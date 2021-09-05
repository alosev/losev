package ru.losev.developerslife.ui.post.wrapper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.tabs.TabLayout
import ru.losev.developerslife.R
import ru.losev.developerslife.databinding.FragmentPostWrapperBinding
import ru.losev.developerslife.model.service.PostCategoryTab
import ru.losev.developerslife.ui.post.category.PostCategoryFragment

class PostWrapperFragment : Fragment() {

    private var _binding: FragmentPostWrapperBinding? = null
    private val binding: FragmentPostWrapperBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPostWrapperBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabs()
        initData()
    }

    private fun initTabs() {
        binding.postCategoryTabs.apply {
            PostCategoryTab.values().forEach { categoryTab ->
                addTab(newTab().setText(categoryTab.title))
            }

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let { selectTab ->
                        onSelectTab(selectTab)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    private fun initData() {
        setFragment(PostCategoryFragment.newInstance(PostCategoryTab.LATEST))
    }

    private fun onSelectTab(tab: TabLayout.Tab) {
        val categories = PostCategoryTab.values()
        if (tab.position >= categories.size) return
        setFragment(PostCategoryFragment.newInstance(categories[tab.position]))
    }

    private fun setFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.post_wrapper_container, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    companion object {
        fun newInstance(): PostWrapperFragment =
            PostWrapperFragment()
    }
}