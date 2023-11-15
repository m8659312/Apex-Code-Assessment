package com.apex.codeassesment.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apex.codeassesment.R
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.databinding.ActivityMainBinding
import com.apex.codeassesment.di.MainComponent
import com.apex.codeassesment.utils.navigateDetails
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import javax.inject.Inject


// => Done TODO (5 points): Move calls to repository to Presenter or ViewModel.
// => Done TODO (5 points): Use combination of sealed/Dataclasses for exposing the data required by the view from viewModel .
// => Done TODO (3 points): Add tests for viewmodel or presenter.
// => Done TODO (1 point): Add content description to images
// TODO (3 points): Add tests
// TODO (Optional Bonus 10 points): Make a copy of this activity with different name and convert the current layout it is using in
//  Jetpack Compose.

class MainActivity : AppCompatActivity() {

    // => Done TODO (2 points): Convert to view binding


    @Inject
    lateinit var viewModel: MainActivityViewModel

    private var randomUser: User = User()
        set(value) {
            // => Done TODO (1 point): Use Glide to load images after getting the data from endpoints mentioned in RemoteDataSource
            // userImageView.setImageBitmap(refreshedUser.image)

            binding.apply {
                Glide.with(this@MainActivity).load(value.picture?.medium).into(mainImage)
                    .onLoadFailed(getDrawable(R.drawable.ic_person))
                mainName.text = value.name!!.first
                mainEmail.text = value.email
                field = value
            }

        }

    lateinit var binding: ActivityMainBinding
    private lateinit var context: Context

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context = this
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        (applicationContext as MainComponent.Injector).mainComponent.inject(this)

        binding.apply {

            mainUserList.layoutManager = LinearLayoutManager(context)

            val recyclerAdapter = RecyclerAdapter(context)
            mainUserList.addItemDecoration(
                DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL
                )
            )

            mainUserList.adapter = recyclerAdapter

            mainSeeDetailsButton.setOnClickListener { this@MainActivity.navigateDetails(randomUser) }

            mainRefreshButton.setOnClickListener {
                viewModel.refreshUser()

            }
            mainUserListButton.setOnClickListener { viewModel.getUsersList() }

            lifecycleScope.launch {
                viewModel.channel.collect {
                    when (it) {
                        is UiEvent.GetSavedUser -> {
                            randomUser = it.user
                        }

                        is UiEvent.GetUserList -> {
//                            arrayAdapter.clear()
//                            arrayAdapter.addAll(it.users)

                            recyclerAdapter.notifyChange(it.users)
                            recyclerAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
            viewModel.initialize()


        }
    }

    // => Done TODO (2 points): Convert to extenstion function.


    class RecyclerAdapter(
        private val context: Context,
    ) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

        private val userList: ArrayList<User> = ArrayList()

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView

            init {
                // Define click listener for the ViewHolder's View
                textView = view.findViewById(android.R.id.text1)
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(android.R.layout.simple_list_item_1, viewGroup, false)

            return ViewHolder(view)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element

            Log.d("mLogs", "onBindViewHolder: ${userList[position]}")
            viewHolder.textView.text = userList[position].toString()

            viewHolder.itemView.setOnClickListener {
                context.navigateDetails(userList[position])
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = userList.size

        fun notifyChange(users: List<User>) {
            userList.clear()
            userList.addAll(users)

            Log.d("mLogs", "notifyChange: size - ${userList.size}")
        }
    }
}









