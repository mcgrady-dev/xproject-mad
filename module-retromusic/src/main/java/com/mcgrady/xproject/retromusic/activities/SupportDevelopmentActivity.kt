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
package com.mcgrady.xproject.retromusic.activities

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.PurchaseInfo
import com.anjlab.android.iab.v3.SkuDetails
import com.mcgrady.xproject.retromusic.BuildConfig
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.activities.base.AbsThemeActivity
import com.mcgrady.xproject.retromusic.databinding.ActivityDonationBinding
import com.mcgrady.xproject.retromusic.databinding.ItemDonationOptionBinding
import com.mcgrady.xproject.retromusic.extensions.*
import com.mcgrady.xproject.theme.util.ATHUtil
import com.mcgrady.xproject.theme.util.TintHelper
import com.mcgrady.xproject.theme.util.ToolbarContentTintHelper

class SupportDevelopmentActivity : AbsThemeActivity(), BillingProcessor.IBillingHandler {

    lateinit var binding: ActivityDonationBinding

    companion object {
        val TAG: String = SupportDevelopmentActivity::class.java.simpleName
        const val DONATION_PRODUCT_IDS = R.array.donation_ids
    }

    var billingProcessor: BillingProcessor? = null

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun donate(i: Int) {
        val ids = resources.getStringArray(DONATION_PRODUCT_IDS)
        billingProcessor?.purchase(this, ids[i])
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColorAuto()
        setTaskDescriptionColorAuto()

        setupToolbar()

        billingProcessor = BillingProcessor(this, BuildConfig.GOOGLE_PLAY_LICENSING_KEY, this)
        TintHelper.setTint(binding.progress, accentColor())
        binding.donation.setTextColor(accentColor())
    }

    private fun setupToolbar() {
        val toolbarColor = surfaceColor()
        binding.toolbar.setBackgroundColor(toolbarColor)
        ToolbarContentTintHelper.colorBackButton(binding.toolbar)
        setSupportActionBar(binding.toolbar)
    }

    override fun onBillingInitialized() {
        loadSkuDetails()
    }

    private fun loadSkuDetails() {
        binding.progressContainer.isVisible = true
        binding.recyclerView.isVisible = false
        val ids = resources.getStringArray(DONATION_PRODUCT_IDS)
        billingProcessor!!.getPurchaseListingDetailsAsync(
            ArrayList(listOf(*ids)),
            object : BillingProcessor.ISkuDetailsResponseListener {
                override fun onSkuDetailsResponse(skuDetails: MutableList<SkuDetails>?) {
                    if (skuDetails == null || skuDetails.isEmpty()) {
                        binding.progressContainer.isVisible = false
                        return
                    }

                    binding.progressContainer.isVisible = false
                    binding.recyclerView.apply {
                        itemAnimator = DefaultItemAnimator()
                        layoutManager = GridLayoutManager(this@SupportDevelopmentActivity, 2)
                        adapter = SkuDetailsAdapter(this@SupportDevelopmentActivity, skuDetails)
                        isVisible = true
                    }
                }

                override fun onSkuDetailsError(error: String?) {
                    Log.e(TAG, error.toString())
                }
            }
        )
    }

    override fun onProductPurchased(productId: String, details: PurchaseInfo?) {
        // loadSkuDetails();
        showToast(R.string.thank_you)
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        Log.e(TAG, "Billing error: code = $errorCode", error)
    }

    override fun onPurchaseHistoryRestored() {
        // loadSkuDetails();
        showToast(R.string.restored_previous_purchases)
    }

    override fun onDestroy() {
        billingProcessor?.release()
        super.onDestroy()
    }
}

class SkuDetailsAdapter(
    private var donationsDialog: SupportDevelopmentActivity,
    objects: List<SkuDetails>,
) : RecyclerView.Adapter<SkuDetailsAdapter.ViewHolder>() {

    private var skuDetailsList: List<SkuDetails> = ArrayList()

    init {
        skuDetailsList = objects
    }

    private fun getIcon(position: Int): Int {
        return when (position) {
            0 -> R.drawable.ic_cookie
            1 -> R.drawable.ic_take_away
            2 -> R.drawable.ic_take_away_coffe
            3 -> R.drawable.ic_beer
            4 -> R.drawable.ic_fast_food_meal
            5 -> R.drawable.ic_popcorn
            6 -> R.drawable.ic_card_giftcard
            7 -> R.drawable.ic_food_croissant
            else -> R.drawable.ic_card_giftcard
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            ItemDonationOptionBinding.inflate(
                LayoutInflater.from(donationsDialog),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val skuDetails = skuDetailsList[i]
        with(viewHolder.binding) {
            itemTitle.text = skuDetails.title.replace("(Retro Music Player MP3 Player)", "")
                .trim { it <= ' ' }
            itemText.text = skuDetails.description
            itemText.isVisible = false
            itemPrice.text = skuDetails.priceText
            itemImage.setImageResource(getIcon(i))
        }

        val purchased = donationsDialog.billingProcessor!!.isPurchased(skuDetails.productId)
        val titleTextColor = if (purchased) ATHUtil.resolveColor(
            donationsDialog,
            android.R.attr.textColorHint
        ) else donationsDialog.textColorPrimary()
        val contentTextColor =
            if (purchased) titleTextColor else donationsDialog.textColorSecondary()

        with(viewHolder.binding) {
            itemTitle.setTextColor(titleTextColor)
            itemText.setTextColor(contentTextColor)
            itemPrice.setTextColor(titleTextColor)
            strikeThrough(itemTitle, purchased)
            strikeThrough(itemText, purchased)
            strikeThrough(itemPrice, purchased)
        }

        viewHolder.itemView.isEnabled = !purchased
        viewHolder.itemView.setOnClickListener { donationsDialog.donate(i) }
    }

    override fun getItemCount(): Int {
        return skuDetailsList.size
    }

    class ViewHolder(val binding: ItemDonationOptionBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private fun strikeThrough(textView: TextView, strikeThrough: Boolean) {
            textView.paintFlags =
                if (strikeThrough) textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                else textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}
