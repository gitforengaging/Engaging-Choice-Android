<p align="center" >
<img src="https://engagingchoice-qa.kiwireader.com/static/img/logo%402x.png" title="Engaging Choice logo" float=left>
</p>


# EngagingChoice

This library provides a Grid View to show media content and poweredby view to show Engaging Choice icon.

Features

EngagingChoiceKey is singletone class and it will use to set Secret key,Email Id and ContentId.
Class for ECGridView
Class for ECPoweredByView to show Engaging Choice icon
ListenerOfEcContentApi is interface which has to be implement where we need EngagingChoice Content Data.
OfferListActivity is Activity which shows Offers.

## Requirements

- Android minimum SDK version 19

- Enable Databinding in Build.gradle

  dataBinding {
  
        enabled = true
        
    }


# How To Use

NOTE: You must provide a EmailId and SecretKey via EngagingChoiceKey class.

1. FIRST STEP
EngagingChoiceKey

Provide Secret key for configuration

EngagingChoiceKey.getInstance().setPublishSecretKey(Constants.TOKEN_QA_VALUE);

Replace SECRET_KEY with  Constants.TOKEN_QA_VALUE

2. SECOND STEP

EcMediaContent

Offset and limit are optional but you can use these fields for pagination by passing offset and limit in param.

EcMediaContent.getInstance().setPageLimit(0,10);

And you need to call below methods to get detail of mediaContent 

Implement ListenerOfEcContentApi interface

EcMediaContent.getInstance().setListenerOfResponse(listener);

EcMediaContent.getInstance().callEcContentApi();



// Get Media Content detail

3. THIRD STEP
- ECGridView uses
In xml file you can use this view as mentioned below with custom height and width:

 <com.aap.engagingchoice.customviews.EcGridView
 
            android:id="@+id/row_of_engaging_ec_view"
            
            android:layout_width="@dimen/dp_95"
            
            android:layout_height="@dimen/dp_120"
            
            />

Below three methods can be used from the id of this view :

// set scale type 

  public void setScaleType(ImageView.ScaleType scaleType) {
  
        mCoverIv.setScaleType(scaleType);
        
    }
    
// load image

    public void setloadUrl(Context context, String url) {
    
        Glide.with(context).load(url).into(mCoverIv);
        
    }
    
// set visibility of Powered by icon

    public void setVisibilityOfPoweredBy(int visibility) {
    
        mPoweredByIv.setVisibility(visibility);
        
    }
    

4. FOURTH STEP

- EcPoweredByView uses

In xml file you can use this view as mentioned below with custom height and width:

 <com.aap.engagingchoice.customviews.EcPoweredByView
 
                    android:id="@+id/detail_tv_powered_by"
                    
                    android:layout_width="wrap_content"
                    
                    android:layout_height="wrap_content"
                    
                    android:layout_below="@+id/detail_tv_channel"
                    
                    android:layout_marginLeft="@dimen/dp_20"
                    
                    android:layout_marginTop="@dimen/dp_3" 
                    
                    />
                   
5.FIFTH STEP

-OFFERS LIST

Implement the EcCallBackOfOfferListener listener to get callback after user has viewed the Offer

CallBackListenerClass.getInstance().setmListener(this);                                                                    

You can call offerlist by below mention method

private void goToOfferListActivity() {
        Intent intent = new Intent(this, OfferListActivity.class);
        startActivity(intent);
    }

## Example

To run the example project, clone the repo, and run.



## Installation

EngagingChoice is available https://bintray.com/engagingchoice1994/EngagingChoiceSDK/engagingchoice

you can simply add the follow line to your build.gradle.

compile 'com.aap.engagingchoice:engagingchoicesdk:1.0.0'




## Author

EngagingChoice, gitforengaging@gmail.com

## License

EngagingChoice is available under the MIT license. See the LICENSE file for more info.
