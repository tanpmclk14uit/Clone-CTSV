package com.example.svbookmarket.activities.animation

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.svbookmarket.R

class TranslateAnimationUtil(context: android.content.Context, viewAnimation: View) :View.OnTouchListener {

    private var gestureDetector: GestureDetector = GestureDetector(context, SimpleGestureDetector(viewAnimation) )

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
    public  class SimpleGestureDetector(private var viewAnimation: View) :
        GestureDetector.SimpleOnGestureListener() {
        private var isFinishAnimation = true;


        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if(distanceY > 0){
                hiddenView();
            }else{
                showView();
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        private fun showView() {
            if(viewAnimation.visibility == View.VISIBLE){
                return
            }
            var animUp: Animation = AnimationUtils.loadAnimation(viewAnimation.context, R.anim.move_up)
            animUp.setAnimationListener(object: Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                    viewAnimation.visibility = View.VISIBLE
                    isFinishAnimation = false;
                }

                override fun onAnimationEnd(animation: Animation?) {
                    isFinishAnimation = true;
                }

                override fun onAnimationStart(animation: Animation?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
            if(isFinishAnimation){
                viewAnimation.startAnimation(animUp)
            }
        }

        private fun hiddenView() {
           if(viewAnimation.visibility == View.GONE){
               return
           }
            var animDow: Animation = AnimationUtils.loadAnimation(viewAnimation.context, R.anim.move_down)
            animDow.setAnimationListener(object: Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                   viewAnimation.visibility = View.VISIBLE
                    isFinishAnimation = false;
                }

                override fun onAnimationEnd(animation: Animation?) {
                    viewAnimation.visibility = View.GONE
                    isFinishAnimation = true;
                }

                override fun onAnimationStart(animation: Animation?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
            if(isFinishAnimation){
                viewAnimation.startAnimation(animDow)
            }
        }
    }


}





