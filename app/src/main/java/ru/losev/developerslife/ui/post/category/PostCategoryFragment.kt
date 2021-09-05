package ru.losev.developerslife.ui.post.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import ru.losev.developerslife.databinding.FragmentPostCategoryBinding
import ru.losev.developerslife.extensions.toViewVisibility
import ru.losev.developerslife.model.entity.Post
import ru.losev.developerslife.model.service.PostCategoryTab
import ru.losev.developerslife.store.post.category.*

class PostCategoryFragment : Fragment() {

    private val postCategoryStore: PostCategoryStore by inject()

    private var _binding: FragmentPostCategoryBinding? = null
    private val binding: FragmentPostCategoryBinding get() = _binding!!

    private var postCategoryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postCategoryId = requireArguments().getString(POST_CATEGORY_ID)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPostCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initStore()
    }

    private fun initView() {
        binding.empty.apply {
            btnRefresh.setOnClickListener {
                postCategoryStore.dispatch(action = PostCategoryAction.Refresh)
            }
        }

        binding.error.apply {
            btnRefresh.setOnClickListener {
                postCategoryStore.dispatch(action = PostCategoryAction.Refresh)
            }
        }

        binding.prevPost.apply {
            setOnClickListener {
                postCategoryStore.dispatch(action = PostCategoryAction.PrevPost)
            }
        }

        binding.nextPost.apply {
            setOnClickListener {
                postCategoryStore.dispatch(action = PostCategoryAction.NextPost)
            }
        }
    }

    private fun initStore() {
        postCategoryStore.observeState().onEach { state ->
            when (state) {
                PostCategoryState.Undefined -> {
                    renderUndefinedState()
                }
                PostCategoryState.Loading -> {
                    renderLoadingState()
                }
                is PostCategoryState.Empty -> {
                    renderEmptyState(message = state.message)
                }
                is PostCategoryState.Error -> {
                    renderErrorState(message = state.message)
                }
                is PostCategoryState.Data -> {
                    renderDataState(stateData = state.stateData)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        postCategoryStore.observeSideEffect().onEach { effect ->
            when (effect) {
                is PostCategoryEffect.Error -> {
                    showMessage(message = effect.message)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        postCategoryId?.let { category ->
            postCategoryStore.dispatch(action = PostCategoryAction.Init(category = category))
        }
    }

    private fun renderUndefinedState() {
        binding.error.apply {
            root.visibility = View.GONE
        }

        binding.empty.apply {
            root.visibility = View.GONE
        }

        binding.groupContent.apply {
            visibility = View.GONE
        }

        binding.progress.apply {
            root.visibility = View.GONE
        }
    }

    private fun renderLoadingState() {
        binding.error.apply {
            root.visibility = View.GONE
        }

        binding.empty.apply {
            root.visibility = View.GONE
        }

        binding.groupContent.apply {
            visibility = View.GONE
        }

        binding.progress.apply {
            root.visibility = View.VISIBLE
        }
    }

    private fun renderEmptyState(message: String) {
        binding.error.apply {
            root.visibility = View.GONE
        }

        binding.empty.apply {
            root.visibility = View.VISIBLE
            emptyMessage.text = message
        }

        binding.groupContent.apply {
            visibility = View.GONE
        }

        binding.progress.apply {
            root.visibility = View.GONE
        }
    }

    private fun renderErrorState(message: String) {
        binding.error.apply {
            root.visibility = View.VISIBLE
            errorMessage.text = message
        }

        binding.empty.apply {
            root.visibility = View.GONE
        }

        binding.groupContent.apply {
            visibility = View.GONE
        }

        binding.progress.apply {
            root.visibility = View.GONE
        }
    }

    private fun renderDataState(stateData: PostCategoryData) {
        binding.error.apply {
            root.visibility = View.GONE
        }

        binding.empty.apply {
            root.visibility = View.GONE
        }

        binding.groupContent.apply {
            visibility = if (stateData.post != null) View.VISIBLE else View.GONE
        }

        binding.progress.apply {
            root.visibility = stateData.loading.toViewVisibility()
        }

        binding.prevPost.apply {
            visibility = stateData.prevPostExist.toViewVisibility(invisibleType = View.INVISIBLE)
        }

        binding.nextPost.apply {
            visibility = stateData.nextPostExist.toViewVisibility(invisibleType = View.INVISIBLE)
        }

        stateData.post?.let { post ->
            renderPost(post = post)
        }
    }

    private fun renderPost(post: Post) {
        binding.post.apply {
            title.text = post.description

            Glide
                .with(this@PostCategoryFragment)
                .load(post.gifUrl)
                .centerCrop()
                .thumbnail(
                    Glide
                        .with(this@PostCategoryFragment)
                        .load(post.previewUrl)
                        .centerCrop()
                )
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image)
        }
    }

    private fun showMessage(message: String) {
        view?.let { view ->
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val POST_CATEGORY_ID = "post_category_id"

        fun newInstance(postCategoryTab: PostCategoryTab): PostCategoryFragment =
            PostCategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(POST_CATEGORY_ID, postCategoryTab.id)
                }
            }
    }
}