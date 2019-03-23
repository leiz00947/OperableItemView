package com.andova.widget

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.Log

/**
 * Created by Administrator on 2019-03-23.
 *
 * @author kzaxil
 * @since 1.0.0
 */
class Text(bodyText: String?, briefText: String?, bodyColor: Int, briefColor: Int, divider: Int,
           private val param: Param, private val oiv: OperableItemView) {
    private var mRefresh = true
    private var mMaxTextWidth: Int = 0
    private var mTextInterval: Int = 0
    private var mBodyTextColor: Int = 0
    private var mBriefTextColor: Int = 0

    private var mBodyText: String? = null
    private var mBriefText: String? = null
    private val mBodyPaint: TextPaint by lazy { TextPaint() }
    private val mBriefPaint: TextPaint by lazy { TextPaint() }
    private var mBodyStcLayout: StaticLayout? = null
    private var mBriefStcLayout: StaticLayout? = null

    companion object {
        private const val TAG = "OperableItemView_Text"
    }

    init {
        mBodyText = bodyText
        mBriefText = briefText
        mTextInterval = divider
        mBodyTextColor = bodyColor
        mBriefTextColor = briefColor
    }

    fun initBriefPaint(context: Context, typefacePath: String?, textSize: Int) {
        mBriefPaint.color = mBriefTextColor
        try {
            mBriefPaint.typeface = Typeface.createFromAsset(context.assets, typefacePath)
        } catch (e: Exception) {
            Log.i(TAG, "No set special brief text typeface!")
        }
        mBriefPaint.textSize = textSize.toFloat()
        when (param.horizontalGravity()) {
            OIV_GRAVITY_FLAG_CENTER_HORIZONTAL -> mBriefPaint.textAlign = Paint.Align.CENTER
            OIV_GRAVITY_FLAG_RIGHT -> mBriefPaint.textAlign = Paint.Align.RIGHT
            OIV_GRAVITY_FLAG_LEFT -> mBriefPaint.textAlign = Paint.Align.LEFT
            else -> mBriefPaint.textAlign = Paint.Align.LEFT
        }
        mBriefPaint.isAntiAlias = true
    }

    fun initBodyPaint(context: Context, typefacePath: String?, textSize: Int) {
        mBodyPaint.color = mBodyTextColor
        try {
            mBodyPaint.typeface = Typeface.createFromAsset(context.assets, typefacePath)
        } catch (e: Exception) {
            Log.i(TAG, "No set special body text typeface!")
        }
        mBodyPaint.textSize = textSize.toFloat()
        when (param.horizontalGravity()) {
            OIV_GRAVITY_FLAG_CENTER_HORIZONTAL -> mBodyPaint.textAlign = Paint.Align.CENTER
            OIV_GRAVITY_FLAG_RIGHT -> mBodyPaint.textAlign = Paint.Align.RIGHT
            OIV_GRAVITY_FLAG_LEFT -> mBodyPaint.textAlign = Paint.Align.LEFT
            else -> mBodyPaint.textAlign = Paint.Align.LEFT
        }
        mBodyPaint.isAntiAlias = true
    }

    private fun usableMaxTextWidth(widthPx: Int): Int {
        if (widthPx <= 0) return 0
        if (!mRefresh && mMaxTextWidth > 0) return mMaxTextWidth
        mMaxTextWidth = widthPx - oiv.occupiedWidthExceptText()
        return mMaxTextWidth
    }

    fun initStaticLayout(widthPx: Int) {
        if (widthPx <= 0) return
        if (mBriefStcLayout == null || mRefresh) {
            mBriefStcLayout = StaticLayout(if (mBriefText == null) "" else mBriefText, mBriefPaint, briefTextWidth(widthPx), Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
        }
        if (mBodyStcLayout == null || mRefresh) {
            mBodyStcLayout = StaticLayout(if (mBodyText == null) "" else mBodyText, mBodyPaint, bodyTextWidth(widthPx), Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false)
        }
    }

    private fun briefTextWidth(widthPx: Int): Int {
        mBriefText ?: return 0
        return when (param.chain()) {
            OIV_DRAWABLE_CHAIN_STYLE_PACKED -> {
                val width = mBriefPaint.measureText(mBriefText).toInt()
                if (width > usableMaxTextWidth(widthPx)) usableMaxTextWidth(widthPx) else width
            }
            OIV_DRAWABLE_CHAIN_STYLE_SPREAD_INSIDE -> usableMaxTextWidth(widthPx)
            else -> usableMaxTextWidth(widthPx)
        }
    }

    private fun bodyTextWidth(widthPx: Int): Int {
        if (TextUtils.isEmpty(mBodyText)) return 0
        return when (param.chain()) {
            OIV_DRAWABLE_CHAIN_STYLE_PACKED -> {
                val width = mBodyPaint.measureText(mBodyText).toInt()
                if (width > usableMaxTextWidth(widthPx)) usableMaxTextWidth(widthPx) else width
            }
            OIV_DRAWABLE_CHAIN_STYLE_SPREAD_INSIDE -> usableMaxTextWidth(widthPx)
            else -> usableMaxTextWidth(widthPx)
        }
    }

    private inline fun getTextHeight(paint: Paint): Float = paint.descent() - paint.ascent()
    fun lineHeight(): Float = if (TextUtils.isEmpty(mBriefText)) 0f else getTextHeight(mBriefPaint) + mTextInterval.toFloat() + if (TextUtils.isEmpty(mBodyText)) 0f else getTextHeight(mBodyPaint)

    private inline fun briefH(): Int = if (mBriefStcLayout == null || TextUtils.isEmpty(mBriefText)) 0 else mBriefStcLayout?.height
            ?: 0

    private inline fun bodyH(): Int = if (mBodyStcLayout == null || TextUtils.isEmpty(mBodyText)) 0 else mBodyStcLayout?.height
            ?: 0

    fun linesHeight(): Int = briefH() + bodyH() + mTextInterval
    fun bodyStcLayout(): StaticLayout? = mBodyStcLayout
    fun bodyStcLayoutH(): Int = mBodyStcLayout?.height ?: 0
    fun briefStcLayout(): StaticLayout? = mBriefStcLayout
    fun briefStcLayoutH(): Int = mBriefStcLayout?.height ?: 0
}