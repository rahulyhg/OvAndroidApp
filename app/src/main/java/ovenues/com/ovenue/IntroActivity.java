package ovenues.com.ovenue;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;
import ovenues.com.ovenue.fragments.CustomSlide;
import ovenues.com.ovenue.utils.Const;

public class IntroActivity extends MaterialIntroActivity {

    SharedPreferences sharepref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.md_grey_900)
                        .buttonsColor(R.color.md_deep_orange_900)
                        .image(R.drawable.firstandlastscreen)
                        //.title("WELCOME")
                        .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
                        .neededPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                        //.description("Ovenues provides comprehensive event-planning resources that can meet all of your needs no matter what you plan to host.")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       /* showMessage("We provide solutions to make you love your work");*/
                       startActivity(new Intent(IntroActivity.this, Login.class));
                        sharepref.edit().putString(Const.INTRO_DONE,"yes").commit();
                        finish();

                    }
                }, "SKIP"));


        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.transparent)
                .buttonsColor(R.color.first_slide_buttons)
                .image(R.drawable.introone)
                //.title("Browse Venues and Services")
                //.description("Narrow your search by location and event type \n" +
                  //      "Broaden your search by distance from your city\n" +
                    //    "Choose the venue or service that fits your event")
                .build());
       /* new MessageButtonBehaviour(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("We provide solutions to make you love your work");
            }
        }, "Work with love"));*/

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.transparent)
                .buttonsColor(R.color.second_slide_buttons)
                .image(R.drawable.introtwo)
                //.title("Make Selection")
                //.description("Check prices by date, time, and guest count\n" +
                  //      "Customize your options\n" +
                    //    "Then add the item to your cart")
                .build());


        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.transparent)
                        .buttonsColor(R.color.third_slide_buttons)
                        .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
                        .neededPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                        .image(R.drawable.introthree)
                        //.title("Celebrate")
                        //.description("Finalize and book your event \n" +
                          //      "You'll receive a confirmation for \n" +
                            //    "your event on your account.")
                        .build());
               /* new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("SMS,CAMERA,,STORAGE,LOCATION");
                    }
                }, "Permissions"));*/

       // addSlide(new CustomSlide());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.transparent)
                .buttonsColor(R.color.second_slide_buttons)
                .image(R.drawable.introfour)
                //.title("Make Selection")
                //.description("Check prices by date, time, and guest count\n" +
                //      "Customize your options\n" +
                //    "Then add the item to your cart")
                .build());



        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.md_grey_900)
                .buttonsColor(R.color.md_deep_orange_900)
                .image(R.drawable.firstandlastscreen)
                //.title("Get Start.")
                //.description("")
                .build(),

                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                               /* showMessage("We provide solutions to make you love your work");*/
                        startActivity(new Intent(IntroActivity.this, Login.class));
                        finish();
                        sharepref.edit().putString(Const.INTRO_DONE,"yes").commit();
                    }
                }, "DONE"));
    }
    @Override
    public void onFinish() {
        super.onFinish();
        //Toast.makeText(this, "Try this library in your project! :)", Toast.LENGTH_SHORT).show();
        sharepref.edit().putString(Const.INTRO_DONE,"yes").commit();
        startActivity(new Intent(IntroActivity.this, Login.class));
        finish();

    }

    @Override
    public void onBackPressed() {

    }
}