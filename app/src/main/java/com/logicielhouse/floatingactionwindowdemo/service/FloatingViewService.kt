package com.logicielhouse.floatingactionwindowdemo.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import com.logicielhouse.floatingactionwindowdemo.R


/**
 * Created by Abdullah on 1/15/2021.
 */
class FloatingViewService : Service(), View.OnClickListener {

    private var mWindowManager: WindowManager? = null
    private var mFloatingView: View? = null
    private var collapsedView: View? = null
    private var expandedView: View? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        //getting the widget layout from xml using layout inflater
        //getting the widget layout from xml using layout inflater
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null)

        //setting the layout parameters

        val LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        //setting the layout parameters
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        //getting windows services and adding the floating view to it

        //getting windows services and adding the floating view to it
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mWindowManager?.addView(mFloatingView, params)

        //getting the collapsed and expanded view from the floating view
        //getting the collapsed and expanded view from the floating view
        collapsedView = mFloatingView!!.findViewById(R.id.layoutCollapsed)
        expandedView = mFloatingView!!.findViewById(R.id.layoutExpanded)

        //adding click listener to close button and expanded view

        //adding click listener to close button and expanded view
        mFloatingView!!.findViewById<View>(R.id.buttonClose).setOnClickListener(this)
        expandedView?.setOnClickListener(this)

        //adding an touch listener to make drag movement of the floating widget

        //adding an touch listener to make drag movement of the floating widget
        mFloatingView!!.findViewById<View>(R.id.relativeLayoutParent)
            .setOnTouchListener(object : OnTouchListener {
                private var initialX = 0
                private var initialY = 0
                private var initialTouchX = 0f
                private var initialTouchY = 0f
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            initialX = params.x
                            initialY = params.y
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                            return true
                        }
                        MotionEvent.ACTION_UP -> {
                            //when the drag is ended switching the state of the widget
                            collapsedView?.visibility = View.GONE
                            expandedView?.visibility = View.VISIBLE
                            return true
                        }
                        MotionEvent.ACTION_MOVE -> {
                            //this code is helping the widget to move around the screen with fingers
                            params.x = initialX + (event.rawX - initialTouchX).toInt()
                            params.y = initialY + (event.rawY - initialTouchY).toInt()
                            mWindowManager!!.updateViewLayout(mFloatingView, params)
                            return true
                        }
                    }
                    return false
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mFloatingView != null) mWindowManager?.removeView(mFloatingView)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.layoutExpanded -> {
                //switching views
                collapsedView!!.visibility = View.VISIBLE
                expandedView!!.visibility = View.GONE
            }
            R.id.buttonClose ->                 //closing the widget
                stopSelf()
        }
    }

}