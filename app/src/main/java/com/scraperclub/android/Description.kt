package com.scraperclub.android

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.android.synthetic.main.fragment_description.*
import kotlin.properties.Delegates


private const val PAGES_COUNT = 4
private val descriptionIcons = arrayOf(
        R.drawable.register_device,
        R.drawable.token,
        R.drawable.upload_target,
        R.drawable.check_status);

private val descriptionTexts = arrayOf(
        "Scraper Club is a network of people who share their device while it is not in use so they can use the power of the network when they need.",

        "Earn Credits by leaving the app open in scraping mode.<br>P.S. make sure to keep it plugged in as the screen must be on for scraping to work.",

        "Spend your credits by logging into <a href=\"https://android.scraperclub.com/home/\">ScraperClub</a> and uploading your list of scraping targets.<br>" +
                "P.S. Everything can be automated with our <a href=\"https://android.scraperclub.com/docs/\">api</a>.",

        "You can check the status of your scraping targets under <a href=\"https://android.scraperclub.com/urls/?pool=all\">/my urls</a>. " +
        "Once they have been processed by your fellow Scraper Club members you can download the raw html under " +
        "<a href=\"https://android.scraperclub.com/scrapes/\">/my scrapes</a>.<br>Or again you can do everything via the api."
);


class DescriptionActivity : AppCompatActivity() {

    private lateinit var mPaginator: LinearLayout
    private lateinit var mPager: ViewPager
    private lateinit var mActionButton: Button

    private var currentIndex: Int by Delegates.observable(0) { _, oldValue, newValue ->
        mPaginator.getChildAt(oldValue).let { it as ImageView }.setImageResource(R.drawable.circle)
        mPaginator.getChildAt(newValue).let { it as ImageView }.setImageResource(R.drawable.circle_filled)
        if (newValue == PAGES_COUNT - 1) {
            mActionButton.setText("Start")
        } else if (oldValue == PAGES_COUNT - 1) {
            mActionButton.setText("SKIP")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        mActionButton = skip_description_button
        mPager = description_pager
        mPaginator = paginator

        val pagerAdapter = ScreenSlidePageAdapter(supportFragmentManager)
        mPager.adapter = pagerAdapter

        for (i in 1..PAGES_COUNT) {
            val imageView = ImageView(this)
            imageView.setImageResource(R.drawable.circle)
            mPaginator.addView(imageView)
        }
        currentIndex = 0
        mPager.addOnPageChangeListener(ViewPageListener())
        mActionButton.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun onBackPressed() {
        if (mPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            mPager.currentItem = mPager.currentItem - 1
        }
    }

    private inner class ScreenSlidePageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = PAGES_COUNT
        override fun getItem(position: Int): Fragment {
            return DescriptionPageFragment(descriptionIcons[position], descriptionTexts[position])
        }
    }

    private inner class ViewPageListener : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(p0: Int) {}
        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
        override fun onPageSelected(p0: Int) {
            currentIndex = p0
        }
    }

    @SuppressLint("ValidFragment")
    class DescriptionPageFragment(val iconResId: Int, val html: String) : Fragment() {

        override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View = inflater.inflate(R.layout.fragment_description, container, false)

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            description_icon.setImageResource(iconResId)
            description_text.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(html)
            }
            description_text.movementMethod=LinkMovementMethod.getInstance()
        }
    }
}

