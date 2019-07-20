package com.sayed.newsapp.ui.home


import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.sayed.newsapp.R
import com.sayed.newsapp.databinding.FragmentNewsDetailBinding
import com.sayed.newsapp.di.Injectable
import com.sayed.newsapp.model.News
import com.sayed.newsapp.ui.base.BaseFragment
import android.net.Uri
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.FacebookSdk.getApplicationContext
import com.flipboard.bottomsheet.commons.IntentPickerSheetView
import com.facebook.share.widget.ShareDialog
import com.facebook.share.model.ShareLinkContent
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.FacebookCallback




class NewsDetailFragment : BaseFragment(),Injectable {

    //Init Data
    lateinit var binding: FragmentNewsDetailBinding
    lateinit var news: News

    //Sharing
    lateinit var shareDialogFacebook: ShareDialog
    lateinit var callbackManager: CallbackManager


    /**
     * ON ACTIVITY RESULT *************************************
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * START ON CREATE VIEW ************************************
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_news_detail, container, false)
        return binding.root
    }

    /**
     * ON VIEW CREATED *************************************
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        news= NewsDetailFragmentArgs.fromBundle(this.arguments!!).newsArgs!! //get news
        initData() //set data

    }

    //Set Data
    private fun initData() {

        updateViews() //update Views
        binding.ivShare.setOnClickListener(shareListener)
        binding.btnContinue.setOnClickListener(continueReadingListener)

    }

    //update Views
    private fun updateViews() {
        //set image with caching
        Glide.with(activity)
            .load(news.urlToImage)
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .into(binding.ivNews)
        binding.tvTitle.setText(news.title) //title
        binding.tvPublishedAt.setText(news.publishedAt.substring(0,10).plus(" ").plus(news.publishedAt.substring(11,16))) //date
        binding.tvSource.setText(news.source.name) //source
        binding.tvDetail.setText(news.description.plus(" ").plus(news.content))
    }

    //Prepare Sharing
    private fun prepareSharing() {
        val imgShareIntent = Intent(Intent.ACTION_SEND)
        imgShareIntent.putExtra(Intent.EXTRA_TEXT,news.url)
        imgShareIntent.setType("text/plain")

        val intentPickerSheet = IntentPickerSheetView(activity, imgShareIntent, "Share...",
            IntentPickerSheetView.OnIntentPickedListener { activityInfo ->
                binding.bottomSheet.dismissSheet()
                if (activityInfo.componentName.packageName == "com.facebook.katana") {

                    FacebookSdk.sdkInitialize(getApplicationContext());
                    callbackManager = CallbackManager.Factory.create();

                    shareDialogFacebook=ShareDialog(activity)
                    val content =ShareLinkContent.Builder()
                                 .setContentUrl(Uri.parse(news.url))
                                 .build()
                    if (ShareDialog.canShow(ShareLinkContent::class.java)) shareDialogFacebook.show(content)

                    shareDialogFacebook.registerCallback(callbackManager, object : FacebookCallback<Sharer.Result> {
                        override fun onSuccess(result: Sharer.Result) {

                        }

                        override fun onCancel() {

                        }

                        override fun onError(error: FacebookException?) {
                            if (error != null && error.message == "null") {
                                // Don't use the app for sharing in case of null-error
                                shareDialogFacebook.show(content, ShareDialog.Mode.WEB)
                            }
                        }
                    })

                } else {
                    startActivity(activityInfo.getConcreteIntent(imgShareIntent))
                }
            })

        // Filter out built in sharing options such as bluetooth and beam.
        intentPickerSheet.setFilter { info -> !info.componentName.packageName.startsWith("com.android") }
        // Sort activities in reverse order for no good reason
        intentPickerSheet.setSortMethod(object : Comparator<IntentPickerSheetView.ActivityInfo> {
            override fun compare(lhs: IntentPickerSheetView.ActivityInfo, rhs: IntentPickerSheetView.ActivityInfo): Int {
                return rhs.label.compareTo(lhs.label)
            }
        })
        binding.bottomSheet.showWithSheetView(intentPickerSheet)
    }


    /**
     * init Listeners ******************************
     */
    var shareListener=object: View.OnClickListener{
        override fun onClick(v: View?) {
            prepareSharing() //Sharing
        }

    }

    var continueReadingListener= object : View.OnClickListener{
        override fun onClick(v: View?) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
            startActivity(browserIntent)
        }
    }


}
