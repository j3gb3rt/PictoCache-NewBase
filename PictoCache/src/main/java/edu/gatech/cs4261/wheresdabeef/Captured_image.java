package edu.gatech.cs4261.wheresdabeef;

import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;

import java.util.Locale;

import edu.gatech.cs4261.wheresdabeef.domain.Image;
import edu.gatech.cs4261.wheresdabeef.rest.RestApiV3;
import edu.gatech.cs4261.wheresdabeef.rest.RestData;


public class Captured_image extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    static Image mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.picture_taken_single);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.taken_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_upload:
                RestApiV3 task = new RestApiV3(getApplication());
                RestData data = new RestData();
                data.setAction(RestData.RestAction.POST_IMAGE);
                data.addParam(new BasicNameValuePair("lat", String.valueOf(mImage.getLatitude())));
                data.addParam(new BasicNameValuePair("lon", String.valueOf(mImage.getLongitude())));
                data.setImage(mImage.getImage());
                data.setThumb(mImage.getThumbnail());

                task.execute(data);

//                RestApiInterface restApiInterface = new RestApiInterface();
//                try {
//                    restApiInterface.saveImage(mImage);
//                }
//                catch (Exception e) {
//                    //do nothing
//                }
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_settings:
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public static void setImage(Image image) {
        mImage = image;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Uri imageLocation = (Uri) getIntent().getExtras().getParcelable("imageLocation");
            Location coordinates = (Location) getIntent().getExtras().getParcelable("coordinates");
            return PlaceholderFragment.newInstance(position + 1, imageLocation, coordinates);
        }

        @Override
        public int getCount() {
            //Show 3 total pages.
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, Uri imageLocation, Location coordinates) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putParcelable("imageLocation", imageLocation);
            args.putParcelable("coordinates", coordinates);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_taken_picture, container, false);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            Uri imageLocation = (Uri) getArguments().getParcelable("imageLocation");
            Bitmap image = ImageAdapter.decodeSampledBitmap(imageLocation, 100, 100);
            imageView.setMaxWidth((int) getResources().getDimension(R.dimen.single_image_width));
            imageView.setImageBitmap(image);
            TextView textView = (TextView) rootView.findViewById(R.id.textView);
            Location coordinates = (Location) getArguments().getParcelable("coordinates");
            double latitude = coordinates.getLatitude();
            double longitude = coordinates.getLongitude();
            Captured_image.setImage(new Image(imageLocation, coordinates));
            textView.setText("Coordinates: " + latitude + ", " + longitude);
            return rootView;
        }
    }

}
