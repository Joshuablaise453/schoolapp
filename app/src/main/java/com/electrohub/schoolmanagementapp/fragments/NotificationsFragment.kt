package com.electrohub.schoolmanagementapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.electrohub.schoolmanagementapp.R

class NotificationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupScrollAnimations(view)
    }

    private fun setupScrollAnimations(view: View) {
        val scrollView = view.findViewById<NestedScrollView>(R.id.notificationsScrollView)
        val contentLayout = view.findViewById<LinearLayout>(R.id.notificationsContentLayout)
        
        contentLayout?.layoutAnimation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_slide_up)
        
        scrollView?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, _, _, _ ->
            val centerY = v.height / 2
            
            for (i in 0 until (contentLayout?.childCount ?: 0)) {
                val child = contentLayout?.getChildAt(i) ?: continue
                val childRect = android.graphics.Rect()
                child.getGlobalVisibleRect(childRect)
                
                val viewCenterY = (childRect.top + childRect.bottom) / 2
                val distFromCenter = (centerY - viewCenterY).toFloat()
                val scale = 1f - (Math.abs(distFromCenter) / v.height) * 0.15f
                val rotation = (distFromCenter / v.height) * 5f
                
                if (childRect.intersect(android.graphics.Rect(0, 0, v.width, v.height))) {
                    child.scaleX = Math.max(0.85f, scale)
                    child.scaleY = Math.max(0.85f, scale)
                    child.rotationX = rotation
                }
            }
        })
    }
}