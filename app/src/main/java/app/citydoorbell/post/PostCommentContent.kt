package app.citydoorbell.post

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.citydoorbell.MainViewModel
import app.citydoorbell.R
import app.citydoorbell.content.ui.IconButtonViewModel
import app.citydoorbell.content.ui.PrimaryIconButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID


@Composable
fun PostContent(
    postReceiver: String,
    mainViewModel: MainViewModel,
    currentText: String,
    title: String,
    destination: String,
    cat: String,
    finished: (String) -> Unit
) {
    var text by remember { mutableStateOf(currentText) }
    var errorMessage by remember { mutableStateOf("") }



    Box(modifier = Modifier
        .background(mainViewModel.appColors.collectAsState().value.backgroundColor)
        .fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PrimaryIconButton(IconButtonViewModel(true, mainViewModel), 48.dp, R.mipmap.sendicon) {
                if (postReceiver == "sendPost") {
                    AppDatabase.addPostToCollection(currentText, cat, destination) {
                        errorMessage = it.second
                        Handler(Looper.getMainLooper()).postDelayed({
                            errorMessage = ""
                            text = ""
                            finished("")
                        }, 3000)
                    }
                } else {
                    AppDatabase.addPostToCollection(currentText, title, destination, mainViewModel.postForComments.value.id) {
                        errorMessage = it.second
                        Handler(Looper.getMainLooper()).postDelayed({
                            errorMessage = ""
                            text = ""
                            finished("")
                        }, 3000)
                    }
                }
            }
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = if (errorMessage == "") mainViewModel.appColors.collectAsState().value.secondaryTextColor else  mainViewModel.appColors.collectAsState().value.highlightColor2,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.8.sp
            )
        }
    }
}

object AppDatabase {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    // Reference to the root of the database
    private val rootRef: DatabaseReference = database.reference

    var posts = mutableStateListOf<PostModel>()
    var isLoading = mutableStateOf(true)
    var errorMessage = mutableStateOf<String?>(null)


    private fun getRootRef(): DatabaseReference {
        return rootRef
    }

    fun getPostModelWithId(id: String): PostModel {
        val foundPost = posts.find { it.id.split("/")[1] == id }
        foundPost?.id
        foundPost?.post
        foundPost?.time
        return foundPost ?: PostModel()
    }

    // Fetch posts in a LaunchedEffect
    fun getCollection(destination: String, category: String, isComments: Boolean, postModel: PostModel) {
        if (isComments) {
            rootRef.child("comments").child(destination).child(category).child(postModel.id.split("/")[1]).child("comments").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    posts.clear()
                    if (snapshot.exists()) {
                        for (postSnapshot in snapshot.children) {
                            val post = postSnapshot.getValue(PostModel::class.java)
                            post?.let { posts.add(it) }
                        }
                    }
                    isLoading.value = false // Data fetching complete
                }

                override fun onCancelled(error: DatabaseError) {
                    isLoading.value = false // Stop loading and show error
                    errorMessage.value = error.message
                }
            })
        } else {
            rootRef.child("posts").child(destination).child(category).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    posts.clear()
                    if (snapshot.exists()) {
                        for (postSnapshot in snapshot.children) {
                            // Convert the DataSnapshot to a Map
                            val postSnapshotMap = parseDataSnapshot(postSnapshot)
                            postSnapshotMap?.let { snap ->
                                val id = snap.keys.first().split("/")[0]
                                val post = PostCollectionModel.fromMap(id, snap)
                                post.let {
                                    posts.add(it)
                                }
                            }
                        }
                    }
                    isLoading.value = false // Data fetching complete
                }

                override fun onCancelled(error: DatabaseError) {
                    isLoading.value = false // Stop loading and show error
                    errorMessage.value = error.message
                }
            })
        }
    }
    fun getCollection(destination: String, category: String, isComments: Boolean) {
        if (isComments) {
            rootRef.child("comments").child(destination).child(category).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    posts.clear()
                    if (snapshot.exists()) {
                        for (postSnapshot in snapshot.children) {
                            val post = postSnapshot.getValue(PostModel::class.java)
                            post?.let { posts.add(it) }
                        }
                    }
                    isLoading.value = false // Data fetching complete
                }

                override fun onCancelled(error: DatabaseError) {
                    isLoading.value = false // Stop loading and show error
                    errorMessage.value = error.message
                }
            })
        } else {
            rootRef.child("posts").child(destination).child(category).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    posts.clear()
                    if (snapshot.exists()) {
                        for (postSnapshot in snapshot.children) {
                            // Convert the DataSnapshot to a Map
                            val postSnapshotMap = parseDataSnapshot(postSnapshot)
                            postSnapshotMap?.let { snap ->
                                val id = snap.keys.first().split("/")[0]
                                val post = PostCollectionModel.fromMap(id, snap)
                                post.let {
                                    posts.add(it)
                                }
                            }
                        }
                    }
                    isLoading.value = false // Data fetching complete
                }

                override fun onCancelled(error: DatabaseError) {
                    isLoading.value = false // Stop loading and show error
                    errorMessage.value = error.message
                }
            })
        }
    }
    fun parseDataSnapshot(dataSnapshot: DataSnapshot): Map<String, Any?>? {
        // Parse the DataSnapshot into a Map
        val typeIndicator = object : GenericTypeIndicator<Map<String, Any?>>() {}
        val map = dataSnapshot.getValue(typeIndicator) ?: return null
        // Flatten the nested map
        return flattenMap(map)
    }
    // Helper function to flatten the nested map
    fun flattenMap(map: Map<String, Any?>, parentKey: String = ""): Map<String, Any?> {
        val flatMap = mutableMapOf<String, Any?>()

        for ((key, value) in map) {
            val newKey = if (parentKey.isEmpty()) key else "$parentKey/$key"

            when (value) {
                is Map<*, *> -> {
                    // Recursively flatten nested maps
                    @Suppress("UNCHECKED_CAST")
                    flatMap.putAll(flattenMap(value as Map<String, Any?>, newKey))
                }
                else -> {
                    flatMap[newKey] = value
                }
            }
        }

        return flatMap
    }
    fun addPostToCollection(postContent: String, category: String, destination: String, postModelId: String, isComplete: (Pair<Boolean, String>) -> Unit) {
        val postCollectionModel = PostCollectionModel(postContent)
        val postModelCollectionMap = postCollectionModel.toMap()
        getRootRef().child("comments").child(destination).child(category).child(postModelId.split("/")[1]).child("comments").child(UUID.randomUUID().toString()).setValue(postModelCollectionMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                isComplete(Pair(true, "Post added successfully"))
            } else {
                // Handle errors and provide meaningful messages
                val errorMessage = task.exception?.message ?: "Unknown error occurred"
                isComplete(Pair(false, "Try again. We were not able to add: $errorMessage"))
            }
        }
    }
    fun addPostToCollection(postContent: String, category: String, destination: String, isComplete: (Pair<Boolean, String>) -> Unit) {
        val postCollectionModel = PostCollectionModel(postContent)
        val postModelCollectionMap = postCollectionModel.toMap()
        getRootRef().child("posts").child(destination).child(category).child(postCollectionModel.getUUID()).setValue(postModelCollectionMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                isComplete(Pair(true, "Post added successfully"))
            } else {
                // Handle errors and provide meaningful messages
                val errorMessage = task.exception?.message ?: "Unknown error occurred"
                isComplete(Pair(false, "Try again. We were not able to add: $errorMessage"))
            }
        }
    }

    class PostModel(var id: String = "", var post: String = "", var time: String = "")
    class PostCollectionModel(postContent: String) {
        private val uuid: UUID = UUID.randomUUID()
        private val timeStamp: String = LocalTime.now().toString()
        private val id: String = "$timeStamp/$uuid"
        private val time: String = "hh:mm a".getFormattedCurrentTime()
        val post: String = postContent

        fun getUUID(): String {
            return uuid.toString()
        }

        fun getId(): String {
            return id.replace(".", ":")
        }
        // Function to convert the model to a map
        fun toMap(): Map<String, Any?> {
            return mapOf(
                "id" to getId(),
                "time" to time,
                "post" to post
            )
        }

        companion object {
            fun fromMap(idd: String, value: Any?): PostModel {
                 // Extract values from the nested map
                val map = value as Map<*, *>
                val post = map["post"] as? String ?: ""
                val id = map["id"] as? String ?: ""
                val time = map["time"] as? String ?: ""

                return PostModel(id, post, time)
            }
        }

        // Function to convert LocalTime.now() and return formatted time stamp
        private fun String.getFormattedCurrentTime(): String {
            // Get the current time
            val now = LocalTime.now()
            // Define the formatter with the given pattern
            val formatter = DateTimeFormatter.ofPattern(this)
            // Format the current time
            return now.format(formatter)
        }
    }
}
