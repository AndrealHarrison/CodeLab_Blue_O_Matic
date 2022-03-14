package com.example.background;

import static com.example.background.Constants.KEY_IMAGE_URI;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.app.Application;
import android.content.ContentResolver;
@@ -26,21 +31,30 @@
        import android.net.Uri;
        import android.text.TextUtils;

        import com.example.background.workers.BlurWorker;

public class BlurViewModel extends ViewModel {

    private Uri mImageUri;
    private WorkManager mWorkManager;

    public BlurViewModel(@NonNull Application application) {
        super();
        mImageUri = getImageUri(application.getApplicationContext());
        mWorkManager = WorkManager.getInstance(application);
    }

    /**
     * Create the WorkRequest to apply the blur and save the resulting image
     * @param blurLevel The amount to blur the image
     */
    void applyBlur(int blurLevel) {
        OneTimeWorkRequest blurRequest =
                new OneTimeWorkRequest.Builder(BlurWorker.class)
                        .setInputData(createInputDataForUri())
                        .build();

        mWorkManager.enqueue(blurRequest);
    }

    private Uri uriOrNull(String uriString) {
        @@ -50,6 +64,18 @@ private Uri uriOrNull(String uriString) {
            return null;
        }

        /**
         * Creates the input data bundle which includes the Uri to operate on
         * @return Data which contains the Image Uri as a String
         */
        private Data createInputDataForUri() {
            Data.Builder builder = new Data.Builder();
            if (mImageUri != null) {
                builder.putString(KEY_IMAGE_URI, mImageUri.toString());
            }
            return builder.build();
        }

        private Uri getImageUri(Context context) {
            Resources resources = context.getResources();

            Uri imageUri = new Uri.Builder()
                    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                    .authority(resources.getResourcePackageName(R.drawable.android_cupcake))
                    .appendPath(resources.getResourceTypeName(R.drawable.android_cupcake))
                    .appendPath(resources.getResourceEntryName(R.drawable.android_cupcake))
                    .build();
            return imageUri;
        }
        /**
         * Getters
         */
        Uri getImageUri() {
            return mImageUri;
        }
    }