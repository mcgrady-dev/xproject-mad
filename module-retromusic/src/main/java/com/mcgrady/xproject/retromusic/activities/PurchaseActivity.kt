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

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.PurchaseInfo
import com.mcgrady.xproject.retromusic.App
import com.mcgrady.xproject.retromusic.BuildConfig
import com.mcgrady.xproject.retromusic.Constants.PRO_VERSION_PRODUCT_ID
import com.mcgrady.xproject.retromusic.R
import com.mcgrady.xproject.retromusic.activities.base.AbsThemeActivity
import com.mcgrady.xproject.retromusic.databinding.ActivityProVersionBinding
import com.mcgrady.xproject.retromusic.extensions.accentColor
import com.mcgrady.xproject.retromusic.extensions.setLightStatusBar
import com.mcgrady.xproject.retromusic.extensions.setStatusBarColor
import com.mcgrady.xproject.retromusic.extensions.showToast
import com.mcgrady.xproject.theme.util.MaterialUtil

class PurchaseActivity : AbsThemeActivity(), BillingProcessor.IBillingHandler {

    private lateinit var binding: ActivityProVersionBinding
    private lateinit var billingProcessor: BillingProcessor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProVersionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarColor(Color.TRANSPARENT)
        setLightStatusBar(false)
        binding.toolbar.navigationIcon?.setTint(Color.WHITE)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        binding.restoreButton.isEnabled = false
        binding.purchaseButton.isEnabled = false

        billingProcessor = BillingProcessor(this, BuildConfig.GOOGLE_PLAY_LICENSING_KEY, this)

        MaterialUtil.setTint(binding.purchaseButton, true)

        binding.restoreButton.setOnClickListener {
            restorePurchase()
        }
        binding.purchaseButton.setOnClickListener {
            billingProcessor.purchase(this@PurchaseActivity, PRO_VERSION_PRODUCT_ID)
        }
        binding.bannerContainer.backgroundTintList =
            ColorStateList.valueOf(accentColor())
    }

    private fun restorePurchase() {
        showToast(R.string.restoring_purchase)
        billingProcessor.loadOwnedPurchasesFromGoogleAsync(object :
                BillingProcessor.IPurchasesResponseListener {
                override fun onPurchasesSuccess() {
                    onPurchaseHistoryRestored()
                }

                override fun onPurchasesError() {
                    showToast(R.string.could_not_restore_purchase)
                }
            })
    }

    override fun onProductPurchased(productId: String, details: PurchaseInfo?) {
        showToast(R.string.thank_you)
        setResult(RESULT_OK)
    }

    override fun onPurchaseHistoryRestored() {
        if (App.isProVersion()) {
            showToast(R.string.restored_previous_purchase_please_restart)
            setResult(RESULT_OK)
        } else {
            showToast(R.string.no_purchase_found)
        }
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        Log.e(TAG, "Billing error: code = $errorCode", error)
    }

    override fun onBillingInitialized() {
        binding.restoreButton.isEnabled = true
        binding.purchaseButton.isEnabled = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        billingProcessor.release()
        super.onDestroy()
    }

    companion object {
        private const val TAG: String = "PurchaseActivity"
    }
}
