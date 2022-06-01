/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcgrady.xproject.common.widget.progressview

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.mcgrady.xproject.common.widget.R
import com.mcgrady.xproject.common.widget.progressview.animation.AnimationDrawingState
import com.mcgrady.xproject.common.widget.progressview.animation.DefaultAnimationOrchestrator
import com.mcgrady.xproject.common.widget.progressview.animation.IAnimatorInterface
import com.mcgrady.xproject.common.widget.progressview.animation.ViewAnimationOrchestrator
import com.mcgrady.xproject.common.widget.util.UIUtil
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.*

/**
 * Created by mcgrady on 2021/05/26.
 */
open class ProgressCircularImageView @JvmOverloads constructor(
    context: Context?,
    attributeSet: AttributeSet? = null,
    defStyleInt: Int = 0
) : AppCompatImageView(context!!, attributeSet, defStyleInt) {

    private val decFormat = DecimalFormat("#.##")

    // 是否锁定
    private var isBlock = false
        set(value) {
            if (field == value)
                return

            field = value
            setup()
        }

    private val initialsRect = Rect()

    private val bitmapPaint = Paint()

    private val mRectF = RectF()

    // 中心点
    private var mCenterPoint = Point()

    private val initialsPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 8f
        setup()
    }

    private var defaultSize: Int = 0
    private var mRadius = 0f

    var distanceToBorder = 25
        set(value) {
            field = if (value <= 0) 0 else value
            setup()
        }

    var borderThickness = 12
        set(value) {
            field = if (value <= 0) 0 else value
            setup()
        }

    var highlightedBorderThickness = 16
        set(value) {
            field = if (value <= 0) 0 else value
            setup()
        }

    private val boundsRect = RectF()
    private val boardMatrix = Matrix()
    private var boundsRadius = 0f

    /*********************** Inner Circle Start ***********************/

    private val innerInset
        get() = distanceToBorder + max(
            borderThickness.toFloat(),
            highlightedBorderThickness.toFloat()
        )

    private var innerBitmapWidth = 0
    private var innerBitmapHeight = 0

    private val innerShaderMatrix = Matrix()

    private var innerBitmapShader: BitmapShader? = null

    /**
     * 圆形的背景画笔
     */
    private val innerBgPaint = Paint()

    /**
     * 内圆背景颜色
     */
    private var innerBackgroundColor = Color.TRANSPARENT
        set(value) {
            field = value
            setup()
        }

    /**
     * 内圆图片
     */
    private var innerBitmap: Bitmap? = null

    /**
     * 初始化图片的矩形区域
     */
    private val innerDrawableRect = RectF()

    private var innerDrawableRadius = 0f

    /*********************** Inner Circle End ***********************/

    /*********************** Middle Start ***********************/

    private val middlePaint = Paint()

    private var middleThickness = 0f

    var middleColor = Color.TRANSPARENT
        set(value) {
            field = value
            setup()
        }

    private var middleRadius = 0f

    private val middleRect = RectF()

    /*********************** Middle End ***********************/

    /*********************** Out Ring Start ***********************/

    private var circleAlpha = 0f

    /**
     * 外部圆环背景的画笔
     */
    private var outRingPaint = Paint()

    private var startAngle: Float = 29f
    private var sweepAngle: Float = -327f

    var outRingBgColor = Color.parseColor("#33000000")
        set(value) {
            field = value
            setup()
        }

    var outRingWidth = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        15f,
        resources.displayMetrics
    )
        set(value) {
            field = value
            setup()
        }

    private var progressPaint = Paint()

    // 配置颜色的渐变至
    private var progressGradientColors =
        intArrayOf(
            Color.parseColor("#ffb900"),
            Color.parseColor("#FFFF50"),
            Color.parseColor("#FFB900")
        )

    var progressMaxValue = 0f
        set(value) {
            field = value
            setup()
        }

    var progressCurValue = 0f
        set(value) {
            field = value
            setup()
        }

    var progressWidth = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        15f,
        resources.displayMetrics
    )
        set(value) {
            field = value
            setup()
        }

    var progressTextSize = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        15f,
        resources.displayMetrics
    )
        set(value) {
            field = value
            setup()
        }

    var progressColor = Color.parseColor("#66000000")
        set(value) {
            field = value
            setup()
        }

    private var percent = 0f
    var animTime: Int = 0

    private val animatorInterface: IAnimatorInterface = AnimatorInterface()

    private var animationDrawingState = AnimationDrawingState(0f, 0f)

    private val AnimationDrawingState.rotationInDegrees: Float
        get() {
            return coercedRotationProgress * 360
        }
    private var animator: ValueAnimator? = null
    private val scaleAnimator = ValueAnimator.ofFloat(1f, 0.9f, 1f).apply {
        addUpdateListener {
            this@ProgressCircularImageView.scaleX = it.animatedValue as Float
            this@ProgressCircularImageView.scaleY = it.animatedValue as Float
        }
    }
    var animationOrchestrator: ViewAnimationOrchestrator =
        DefaultAnimationOrchestrator.create().also {
            attachOrchestrator(it)
        }
        set(value) {
                if (field == value) return
                field.cancel()
                field = value
                attachOrchestrator(field)
            }

    private fun attachOrchestrator(animationOrchestrator: ViewAnimationOrchestrator) {
        animationOrchestrator.attach(animatorInterface) {
            if (isReversedAnimating) {
                animationDrawingState = animationDrawingState.copy(
                    rotationProgress = 0f
                )
                isReversedAnimating = false
            }
        }
    }

    private var isReversedAnimating = false
    var isAnimating = false
        set(value) {
            if (value && !field) {
                if (isReversedAnimating) {
                    animationOrchestrator.reverse()
                }
                animationOrchestrator.start()
            } else if (!value && field) {
                isReversedAnimating = true
                animationOrchestrator.cancel()
                animationOrchestrator.reverse()
            }
            field = value
            setup()
        }

    /*********************** Out Ring End ***********************/

    /*********************** Badge Start ***********************/

    // 文字进度
    private var badgerText: String? = null

    // 右下角圆圈的alpha
    private var badgerAlpha = 0f

    var badgeDrawableId = -1
        set(value) {
            field = value
            setup()
        }

    private var badgeShader: BitmapShader? = null
    val badgeIconPaint = Paint()

    private val badgePaint = Paint()
//    private val badgeStrokePaint = Paint()

    /**
     * Flag to toggle visibility of the badge.
     */
    var showBadge = false
        set(value) {
            field = value
            setup()
        }

    /**
     * The color of the badge.
     */
    var badgeColor = Color.parseColor("#66000000")
        set(value) {
            field = value
            setup()
        }

    /**
     * The color of the stroke of the badge.
     */
//    var badgeStrokeColor = Defaults.BADGE_STROKE_COLOR
//        set(value) {
//            field = value
//            setup()
//        }

    /**
     * The stroke width (in pixels) of the badge.
     */
    var badgeStrokeWidth = 0
        set(value) {
            field = if (value <= 0) 0 else value
            setup()
        }

    // 右下角圆圈的半径
    var badgeRadius = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        15f,
        resources.displayMetrics
    )
        set(value) {
            field = value
            setup()
        }

    /**
     * 进度文字的画笔
     */
    private val badgeTextPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 8f
        setup()
    }

    private var badgeDrawable: Bitmap? = null

    /*********************** Badge End ***********************/

    var shouldBounceOnClick = true

    /**
     * Text for which the initial(s) has to be displayed
     */
    var progressText: String? = null
        set(value) {
            field = value
            setup()
        }

    private var initials: String? = null

    private var isReadingAttributes = false

    private var isHighlighted = false
        set(value) {
            field = value
            setup()
        }

    init {
        attributeSet?.let { readAttrs(it, defStyleInt) }

        decFormat.roundingMode = RoundingMode.CEILING
        scaleType = ImageView.ScaleType.CENTER_CROP
        outlineProvider = OutlineProvider()
        defaultSize = UIUtil.dp2px(150.toFloat())
        animator = ValueAnimator()
        setProgress(progressCurValue, progressMaxValue)
        setup()
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        initializeBitmap(bm)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        initializeBitmap(null)
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        initializeBitmap(null)
    }

    @SuppressLint("CustomViewStyleable")
    private fun readAttrs(attrs: AttributeSet, defStyle: Int = 0) {
        isReadingAttributes = true
        val a = context.obtainStyledAttributes(attrs, R.styleable.ProgressCircularView, defStyle, 0)

        initInnerAttrs(a)

        initMiddleAttrs(a)

        initOutRingAttrs(a)

        initBadgeAttrs(a)

        progressColor = a.getColor(R.styleable.ProgressCircularView_cv_progress_color, Color.BLACK)
        progressTextSize = a.getDimension(
            R.styleable.ProgressCircularView_cv_progress_size,
            150f
        )

        circleAlpha = a.getFloat(R.styleable.ProgressCircularView_cv_circle_alpha, 0f)
        animTime = a.getInt(R.styleable.ProgressCircularView_cv_progress_anim_time, 1000)

        a.recycle()
        isReadingAttributes = false
    }

    private fun initInnerAttrs(a: TypedArray) {
        innerBackgroundColor = a.getColor(
            R.styleable.ProgressCircularView_cv_circle_background_color,
            Color.TRANSPARENT
        )
    }

    private fun initMiddleAttrs(a: TypedArray) {
        middleColor =
            a.getColor(R.styleable.ProgressCircularView_cv_middle_color, Color.TRANSPARENT)
    }

    private fun initOutRingAttrs(a: TypedArray) {
        borderThickness = a.getDimensionPixelSize(
            R.styleable.ProgressCircularView_cv_border_thickness,
            12
        )

        distanceToBorder = a.getDimensionPixelSize(
            R.styleable.ProgressCircularView_cv_distance_to_border,
            25
        )

        highlightedBorderThickness = a.getDimensionPixelSize(
            R.styleable.ProgressCircularView_cv_border_thickness_highlight,
            16
        )

        isHighlighted =
            a.getBoolean(R.styleable.ProgressCircularView_cv_highlighted, false)

        outRingBgColor =
            a.getColor(
                R.styleable.ProgressCircularView_cv_outring_bg_color,
                Color.parseColor("#33000000")
            )
        outRingWidth = a.getDimension(
            R.styleable.ProgressCircularView_cv_outring_width,
            15f
        )

        progressWidth = a.getDimension(
            R.styleable.ProgressCircularView_cv_progress_width,
            15f
        )

        progressCurValue =
            a.getFloat(
                R.styleable.ProgressCircularView_cv_progress_cur_value,
                0f
            )

        progressMaxValue = a.getFloat(
            R.styleable.ProgressCircularView_cv_progress_max_value,
            0f
        ) // 100

        startAngle = a.getFloat(
            R.styleable.ProgressCircularView_cv_progress_start_angle,
            29f
        )
        sweepAngle = a.getFloat(
            R.styleable.ProgressCircularView_cv_sweep_angle,
            -327f
        )

        val progressGradientColors: Int =
            a.getResourceId(R.styleable.ProgressCircularView_cv_progress_colors, 0) // 圆弧颜色

        if (progressGradientColors != 0) {
            try {
                val gradientColors = resources.getIntArray(progressGradientColors)

                if (gradientColors.isEmpty()) { // 如果渐变色为数组为0，则尝试以单色读取色值
                    val color = ContextCompat.getColor(context, progressGradientColors)
                    this.progressGradientColors = IntArray(2)
                    this.progressGradientColors[0] = color
                    this.progressGradientColors[1] = color
                } else if (gradientColors.size == 1) { // 如果渐变数组只有一种颜色，默认设为两种相同颜色
                    this.progressGradientColors = IntArray(2)
                    this.progressGradientColors[0] = gradientColors[0]
                    this.progressGradientColors[1] = gradientColors[0]
                } else {
                    this.progressGradientColors = gradientColors
                }
            } catch (e: Resources.NotFoundException) {
                throw Resources.NotFoundException("the give resource not found.")
            }
        }
    }

    private fun initBadgeAttrs(a: TypedArray) {
        showBadge = a.getBoolean(R.styleable.ProgressCircularView_cv_show_badge, false)
        badgeColor = a.getColor(
            R.styleable.ProgressCircularView_cv_badge_color,
            Color.parseColor("#66000000")
        )
//        badgeStrokeColor =
//            a.getColor(R.styleable.CourseView_cv_badge_stroke_color, Defaults.BADGE_STROKE_COLOR)
        badgeStrokeWidth = a.getDimensionPixelSize(
            R.styleable.ProgressCircularView_cv_badge_stroke_width,
            badgeStrokeWidth
        )
        badgeRadius = a.getDimension(R.styleable.ProgressCircularView_cv_badge_radius, badgeRadius)

        badgeTextPaint.textSize =
            a.getDimension(
                R.styleable.ProgressCircularView_cv_badge_text_size,
                initialsPaint.textSize
            )
        badgeTextPaint.color =
            a.getColor(R.styleable.ProgressCircularView_cv_badge_text_color, initialsPaint.color)

        badgerAlpha = a.getFloat(R.styleable.ProgressCircularView_cv_badger_alpha, 0f)
        badgeDrawableId = a.getResourceId(R.styleable.ProgressCircularView_cv_badge_resource, 0)
    }

    private fun setup() {
        if (isReadingAttributes) {
            return
        }
        if (width == 0 && height == 0) {
            return
        }

        val innerDrawable = this.innerBitmap
        if (innerDrawable == null) {
            setImageResource(android.R.color.transparent)
            return
        }

        innerBitmapHeight = innerDrawable.height
        innerBitmapWidth = innerDrawable.width

//        innerBitmapShader =
//            BitmapShader(innerDrawable, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        bitmapPaint.isAntiAlias = true
//        bitmapPaint.shader = innerBitmapShader
//        bitmapPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))

        badgeTextPaint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
        }
        badgeDrawable = BitmapFactory.decodeResource(resources, badgeDrawableId)
//        badgeDrawable?.let {
//            badgeShader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
//            badgeIconPaint.shader = badgeShader
//        }

        val currentBorderThickness =
            (if (isHighlighted) highlightedBorderThickness else borderThickness).toFloat()

        boundsRect.set(calculateBounds())
        boundsRadius = min(
            (boundsRect.height() - currentBorderThickness) / 2.0f,
            (boundsRect.width() - currentBorderThickness) / 2.0f
        )

        setProgressPaint()
        setBorderBgPaint()
//        val currentBorderGradient = LinearGradient(
//            0f, 0f, boundsRect.width(), boundsRect.height(),
//            if (isHighlighted) highlightBorderColor else borderColor,
//            if (isHighlighted) highlightBorderColorEnd else borderColor,
//            Shader.TileMode.CLAMP
//        )
//        borderPaint.apply {
//            shader = currentBorderGradient
//            strokeWidth = currentBorderThickness
//            isAntiAlias = true
//            strokeCap = Paint.Cap.ROUND
//            style = Paint.Style.STROKE
//        }

        innerDrawableRect.set(boundsRect)
        innerDrawableRect.inset(innerInset, innerInset)
        middleThickness =
            (boundsRect.width() - currentBorderThickness * 2 - innerDrawableRect.width()) / 2

        middleRect.set(boundsRect)
        middleRect.inset(
            currentBorderThickness + middleThickness / 2,
            currentBorderThickness + middleThickness / 2
        )

        middleRadius =
            floor(middleRect.height() / 2.0f).coerceAtMost(floor(middleRect.width() / 2.0f))
        innerDrawableRadius =
            (innerDrawableRect.height() / 2.0f).coerceAtMost(innerDrawableRect.width() / 2.0f)

        mCenterPoint.x = boundsRect.centerX().toInt()
        mCenterPoint.y = boundsRect.centerY().toInt()
        // 外部圆环和图片中间部分
        middlePaint.apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
            color = middleColor
            strokeWidth = middleThickness
        }
        // 圆环背景
        innerBgPaint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = innerBackgroundColor
        }

        // 右小角的圈圈
        badgePaint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = badgeColor
        }

//        //右下角的圆形外部圈
//        badgeStrokePaint.apply {
//            style = Paint.Style.FILL
//            isAntiAlias = true
//            color = badgeStrokeColor
//        }
        // 获取位置
        updateShaderMatrix()
        invalidate()
    }

    private fun setBorderBgPaint() {
        outRingPaint.apply {
            isAntiAlias = true
            color = outRingBgColor
            style = Paint.Style.STROKE
            strokeWidth = outRingWidth
            strokeCap = Paint.Cap.ROUND
//            alpha = borderAlpha.toInt()
        }
    }

    private fun setProgressPaint() {
        progressPaint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE // 画圆环必有,不然是扇弧
            strokeWidth = progressWidth
            strokeCap = Paint.Cap.ROUND // 作用于圆环结尾
            isAntiAlias = true
            textSize = progressTextSize
            color = progressColor
            // 设置Typeface对象，即字体风格，包括粗体，斜体以及衬线体，非衬线体等
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }
    }

    private fun updateShaderMatrix() {
//        val scale: Float
//        var dx = 0f
//        var dy = 0f
//
//        innerShaderMatrix.set(null)
//
//        if (innerBitmapWidth * innerDrawableRect.height() > innerDrawableRect.width() * innerBitmapHeight) {
//            scale = innerDrawableRect.height() / innerBitmapHeight.toFloat()
//            dx = (innerDrawableRect.width() - innerBitmapWidth * scale) / 2f
//        } else {
//            scale = innerDrawableRect.width() / innerBitmapWidth.toFloat()
//            dy = (innerDrawableRect.height() - innerBitmapHeight * scale) / 2f
//        }
//
//        innerShaderMatrix.setScale(scale, scale)
//        innerShaderMatrix.postTranslate(
//            (dx + 0.5f).toInt() + innerDrawableRect.left,
//            (dy + 0.5f).toInt() + innerDrawableRect.top
//        )
//
//        innerBitmapShader!!.setLocalMatrix(innerShaderMatrix)
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        return try {
            val bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(
                    2,
                    2,
                    Bitmap.Config.RGB_565
                )
            } else {
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.RGB_565
                )
            }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun initializeBitmap(bm: Bitmap?) {
//        if (bm == null) {
        innerBitmap = getBitmapFromDrawable(drawable)
//        } else {
//            innerBitmap = bm
//        }

        setup()
    }

    private fun calculateBounds(): RectF {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = Math.min(availableWidth, availableHeight)

        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f

        return RectF(left, top, left + sideLength, top + sideLength)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return inTouchableArea(event.x, event.y) && super.onTouchEvent(event)
    }

    private fun animateClick() {
        if (shouldBounceOnClick) scaleAnimator.start()
    }

    override fun performClick(): Boolean {
        animateClick()
        return super.performClick()
    }

    override fun performLongClick(): Boolean {
        animateClick()
        return super.performLongClick()
    }

    private fun inTouchableArea(x: Float, y: Float): Boolean {
        return Math.pow(
            x - boundsRect.centerX().toDouble(),
            2.0
        ) + Math.pow(y - boundsRect.centerY().toDouble(), 2.0) <= Math.pow(
            boundsRadius.toDouble(),
            2.0
        )
    }

    /**
     * 绘制内圆
     */
    private fun drawInner(canvas: Canvas) {

        if (innerBackgroundColor != Color.TRANSPARENT) {
            innerBgPaint.alpha =
                if (isBlock) 128 else 255
            canvas.drawCircle(
                innerDrawableRect.centerX(),
                innerDrawableRect.centerY(),
                innerDrawableRadius,
                innerBgPaint
            )
        }

        if (null != innerBitmap && hasInnerCircleDrawable()) {
            bitmapPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            bitmapPaint.alpha =
                if (isBlock) 128 else 255
//            canvas.drawCircle(
//                innerDrawableRect.centerX(),
//                innerDrawableRect.centerY(),
//                innerDrawableRadius,
//                bitmapPaint
//            )
            canvas.drawBitmap(
                innerBitmap!!,
                0f,
                0f,
                bitmapPaint
            )
        } else if (null != initials) {
            canvas.drawText(
                initials!!,
                width.div(2f) - initialsRect.width().div(2f),
                height.div(2f) + initialsRect.height().div(2f),
                initialsPaint
            )
        }
    }

    private fun drawMiddle(canvas: Canvas) {
        if (middleThickness > 0) {
            canvas.drawCircle(middleRect.centerX(), middleRect.centerY(), middleRadius, middlePaint)
        }
    }

    private fun drawBadge(canvas: Canvas, layerId: Int) {
        if (!showBadge || badgeColor == Color.TRANSPARENT) {
            return
        }

        val radius = badgeRadius.plus(badgeStrokeWidth)

        // X坐标= a + Math.sin(角度 * (Math.PI / 180)) * r
        // Y坐标= b + Math.cos(角数 * (Math.PI / 180)) * r

        // 默认为右下角，即45°
        val badgeCx = mCenterPoint.x + sin(45 * (Math.PI / 180)).toFloat() * mRadius
        val badgeCy = mCenterPoint.y + cos(45 * (Math.PI / 180)).toFloat() * mRadius

        bitmapPaint.color = Color.WHITE
        bitmapPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        canvas.drawCircle(badgeCx, badgeCy, radius, bitmapPaint)
        canvas.restoreToCount(layerId)

        bitmapPaint.xfermode = null
        canvas.drawCircle(badgeCx, badgeCy, badgeRadius, badgePaint)

        if (isBlock) {
            badgeDrawable?.let {
                canvas.drawBitmap(
                    it,
                    badgeCx - badgeRadius,
                    badgeCy - badgeRadius,
                    badgeIconPaint
                )
            }
        } else {
            progressText?.let {
                val rect = Rect()
                badgeTextPaint.getTextBounds(it, 0, it.length, rect)
                // 绘制文字进度
                canvas.drawText(
                    it,
                    badgeCx,
                    badgeCy + rect.height() / 2,
                    badgeTextPaint
                )
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (innerBitmap == null && initials == null) {
            return
        }

        val layerId = canvas.saveLayer(
            0f, 0f,
            canvas.width.toFloat(), canvas.height.toFloat(), null
        )

        drawInner(canvas)

        drawMiddle(canvas)

        drawOutRing(canvas)

        drawBadge(canvas, layerId)
    }

    private fun hasInnerCircleDrawable(): Boolean {
        val drawable = this.drawable
        val hasTransparentDrawable = drawable is ColorDrawable && drawable.alpha == 0
        return !hasTransparentDrawable
    }

    /**
     * 绘制外环和进度条
     */
    private fun drawOutRing(canvas: Canvas) {
        // 绘制背景圆弧， 圆弧给负数就是逆时针，给正数就是顺时针
        canvas.save()

        canvas.rotate(
            startAngle,
            mCenterPoint.x.toFloat(),
            mCenterPoint.y.toFloat()
        )
        canvas.drawArc(mRectF, 0f, sweepAngle, false, outRingPaint)

        if (percent.isNaN() || percent <= 0f) {
            canvas.restore()
            return
        }

        var currentAngle: Float = sweepAngle * percent

        canvas.drawArc(mRectF, 0f, currentAngle, false, progressPaint)

        canvas.restore()
    }

    private fun startAnimator(start: Float, end: Float, animTime: Int?) {
        animator = ValueAnimator.ofFloat(start, end) // 获取%区间 当前进度，[0.0f,1.0f]
        animator?.apply {
            duration = animTime?.toLong() ?: 0
            addUpdateListener { animation ->
                percent = animation?.getAnimatedValue() as Float
                progressCurValue = percent * progressMaxValue
                Log.d(
                    "onAnimationUpdate",
                    "percent = $percent progressCurrentValue = $progressCurValue progressMaxValue = $progressMaxValue"
                )
                invalidate()
            }
            start()
        }
    }

    private fun updateArchesPaint() {
        // 设置渐变
        val mSweepGradient =
            SweepGradient(
                mCenterPoint.x.toFloat(),
                mCenterPoint.y.toFloat(),
                progressGradientColors,
                null
            )
        progressPaint.shader = mSweepGradient
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            UIUtil.measure(widthMeasureSpec, defaultSize),
            UIUtil.measure(heightMeasureSpec, defaultSize)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val maxArcWidth: Float = progressWidth.coerceAtLeast(outRingWidth)
        // 求最小值作为实际值
        val minSize = (w - paddingLeft - paddingRight - 2 * maxArcWidth.toInt())
            .coerceAtMost(h - paddingTop - paddingBottom - 2 * maxArcWidth.toInt())

        // 获取圆心,圆半径
        mRadius = minSize.div(2).toFloat()
        // 获取圆的相关参数
        mCenterPoint.x = w / 2
        mCenterPoint.y = h / 2

        // 绘制圆弧的边界(画圆弧(或圆环)先要画矩形)
        mRectF.left = (mCenterPoint.x - mRadius - maxArcWidth / 2)
        mRectF.top = mCenterPoint.y - mRadius - maxArcWidth / 2
        mRectF.right = mCenterPoint.x + mRadius + maxArcWidth / 2
        mRectF.bottom = mCenterPoint.y + mRadius + maxArcWidth / 2
        // 计算文字绘制时的 baseline
        // 由于文字的baseline、descent、ascent等属性只与textSize和typeface有关，所以此时可以直接计算
        // 若value、hint、unit由同一个画笔绘制或者需要动态设置文字的大小，则需要在每次更新后再次计算
//        valueOffset = (mCenterPoint.y + getBaselineOffsetFromY(mValuePaint)).toInt() //value处在中间
        updateArchesPaint()
        setup()
    }

    /**
     * 用于点击设置随机值,用于进度显示
     */
    fun setProgressWidthAnim(value: Float) {
        setProgress(value, progressMaxValue)

        val start = percent
        val end = progressCurValue / progressMaxValue
        startAnimator(start, end, animTime)
    }

    private fun setProgress(current: Float, max: Float) {
        if (current == progressCurValue && max == progressMaxValue)
            return

        percent = decFormat.format(current.div(max)).toFloat()
        progressCurValue = current
        progressMaxValue = max

        val builder = StringBuilder()
        progressText = builder
            .append(progressCurValue.toInt())
            .append("/")
            .append(progressMaxValue.toInt())
            .toString()

        builder.delete(0, builder.length)
    }

    private fun getBaselineOffsetFromY(paint: Paint): Float {
        return UIUtil.measureTextHeight(paint) / 2
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setup()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        setup()
    }

    fun clear() {
        initialsRect.setEmpty()
        mRectF.setEmpty()
        mCenterPoint.set(0, 0)
        boundsRect.setEmpty()
        innerDrawableRect.setEmpty()
        middleRect.setEmpty()

        initialsPaint.reset()
        bitmapPaint.reset()
        innerBgPaint.reset()
        middlePaint.reset()
        outRingPaint.reset()
        progressPaint.reset()
        badgeIconPaint.reset()
        badgePaint.reset()
        badgeTextPaint.reset()

        boardMatrix.reset()
        innerShaderMatrix.reset()

        innerBitmapShader = null
        badgeShader = null
        badgeDrawable?.recycle()
        badgeDrawable = null
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private inner class OutlineProvider : ViewOutlineProvider() {

        override fun getOutline(view: View, outline: Outline) = Rect().let {
            boundsRect.roundOut(it)
            outline.setRoundRect(it, it.width() / 2.0f)
        }
    }

    inner class AnimatorInterface : IAnimatorInterface {
        private val currentAnimationState: AnimationDrawingState
            get() = animationDrawingState

        override fun updateAnimationState(update: (currentState: AnimationDrawingState) -> AnimationDrawingState) {
            animationDrawingState = update(currentAnimationState)
            invalidate()
        }
    }
}
