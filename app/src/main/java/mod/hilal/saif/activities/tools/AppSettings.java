package mod.hilal.saif.activities.tools;

import static com.besome.sketch.editor.view.ViewEditor.shakeView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.besome.sketch.editor.manage.library.LibraryCategoryView;
import com.besome.sketch.editor.manage.library.LibraryItemView;
import com.besome.sketch.help.SystemSettingActivity;
import com.besome.sketch.lib.base.BaseAppCompatActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import dev.aldi.sayuti.editor.manage.ManageLocalLibraryActivity;
import dev.pranav.filepicker.FilePickerCallback;
import dev.pranav.filepicker.FilePickerDialogFragment;
import dev.pranav.filepicker.FilePickerOptions;
import dev.pranav.filepicker.SelectionMode;
import mod.alucard.tn.apksigner.ApkSigner;
import mod.hey.studios.code.SrcCodeEditor;
import mod.hey.studios.util.Helper;
import mod.khaled.logcat.LogReaderActivity;
import pro.sketchware.R;
import pro.sketchware.activities.editor.component.ManageCustomComponentActivity;
import pro.sketchware.activities.settings.SettingsActivity;
import pro.sketchware.activities.settings.GithubSettingsActivity;
import pro.sketchware.activities.settings.IaSettingsActivity;
import pro.sketchware.databinding.ActivityAppSettingsBinding;
import pro.sketchware.databinding.DialogSelectApkToSignBinding;
import pro.sketchware.utility.AdManager;
import pro.sketchware.utility.FileUtil;
import pro.sketchware.utility.SketchwareUtil;
import pro.sketchware.utility.TranslationFunction;

public class AppSettings extends BaseAppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        enableEdgeToEdgeNoContrast();
        super.onCreate(savedInstanceState);
        var binding = ActivityAppSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        {
            View view = binding.appBarLayout;
            int left = view.getPaddingLeft();
            int top = view.getPaddingTop();
            int right = view.getPaddingRight();
            int bottom = view.getPaddingBottom();

            ViewCompat.setOnApplyWindowInsetsListener(view, (v, i) -> {
                Insets insets = i.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
                v.setPadding(left + insets.left, top + insets.top, right + insets.right, bottom + insets.bottom);
                return i;
            });
        }

        {
            View view = binding.contentScroll;
            int left = view.getPaddingLeft();
            int top = view.getPaddingTop();
            int right = view.getPaddingRight();
            int bottom = view.getPaddingBottom();

            ViewCompat.setOnApplyWindowInsetsListener(view, (v, i) -> {
                Insets insets = i.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(left, top, right, bottom + insets.bottom);
                return i;
            });
        }

        binding.topAppBar.setNavigationOnClickListener(Helper.getBackPressedClickListener(this));
        setupPreferences(binding.content);
    }

    private void setupPreferences(ViewGroup content) {
        LibraryCategoryView managersCategory = new LibraryCategoryView(this);
        managersCategory.setTitle(getString(R.string.auto_str_0270));

        managersCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_block, Helper.getResString(R.string.auto_settings_block_mgr), Helper.getResString(R.string.auto_settings_block_mgr_desc), new ActivityLauncher(new Intent(getApplicationContext(), BlocksManager.class))), true);
        managersCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_pull_down, Helper.getResString(R.string.auto_settings_selector_mgr), Helper.getResString(R.string.auto_settings_selector_mgr_desc), openSettingsActivity(SettingsActivity.BLOCK_SELECTOR_MANAGER_FRAGMENT)), true);
        managersCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_component, Helper.getResString(R.string.auto_settings_component_mgr), Helper.getResString(R.string.auto_settings_component_mgr_desc), new ActivityLauncher(new Intent(getApplicationContext(), ManageCustomComponentActivity.class))), true);
        managersCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_list, Helper.getResString(R.string.auto_settings_event_mgr), Helper.getResString(R.string.auto_settings_event_mgr_desc), openSettingsActivity(SettingsActivity.EVENTS_MANAGER_FRAGMENT)), true);
        managersCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_box, Helper.getResString(R.string.auto_settings_local_lib_mgr), Helper.getResString(R.string.auto_settings_local_lib_mgr_desc), new ActivityLauncher(new Intent(getApplicationContext(), ManageLocalLibraryActivity.class), new Pair<>("sc_id", "system"))), true);
        managersCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_article, Helper.getResString(R.string.design_drawer_menu_title_logcat_reader), Helper.getResString(R.string.design_drawer_menu_subtitle_logcat_reader), new ActivityLauncher(new Intent(getApplicationContext(), LogReaderActivity.class))), false);

        LibraryCategoryView generalCategory = new LibraryCategoryView(this);
        generalCategory.setTitle(getString(R.string.auto_str_0217));

        generalCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_settings_applications, Helper.getResString(R.string.auto_settings_app_settings), Helper.getResString(R.string.auto_settings_app_settings_desc), new ActivityLauncher(new Intent(getApplicationContext(), ConfigActivity.class))), true);
        generalCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_palette, Helper.getResString(R.string.settings_appearance), Helper.getResString(R.string.settings_appearance_description), openSettingsActivity(SettingsActivity.SETTINGS_APPEARANCE_FRAGMENT)), true);
        generalCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_settings, Helper.getResString(R.string.ia_settings_title), Helper.getResString(R.string.ia_settings_subtitle), new ActivityLauncher(new Intent(getApplicationContext(), IaSettingsActivity.class))), true);
        generalCategory.addLibraryItem(createPreference(R.drawable.ic_github, Helper.getResString(R.string.auto_settings_github), Helper.getResString(R.string.auto_settings_github_desc), new ActivityLauncher(new Intent(getApplicationContext(), GithubSettingsActivity.class))), true);
        generalCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_folder, Helper.getResString(R.string.auto_settings_open_wd), Helper.getResString(R.string.auto_settings_open_wd_desc), v -> openWorkingDirectory()), true);
        generalCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_apk_document, Helper.getResString(R.string.auto_settings_sign_apk), Helper.getResString(R.string.auto_settings_sign_apk_desc), v -> signApkFileDialog()), true);
        generalCategory.addLibraryItem(createPreference(R.drawable.ic_mtrl_settings, Helper.getResString(R.string.main_drawer_title_system_settings), Helper.getResString(R.string.auto_settings_autosave), new ActivityLauncher(new Intent(getApplicationContext(), SystemSettingActivity.class))), false);

        // "Managers" card, then the ad between the two cards (never overlapping either),
        // then the "General" card below it.
        content.addView(managersCategory);
        content.addView(createAdSlot());
        content.addView(generalCategory);
    }

    private View createAdSlot() {
        LinearLayout adContainer = new LinearLayout(this);
        adContainer.setOrientation(LinearLayout.VERTICAL);
        adContainer.setGravity(android.view.Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int marginVertical = (int) (8 * getResources().getDisplayMetrics().density);
        params.topMargin = marginVertical;
        params.bottomMargin = marginVertical;
        adContainer.setLayoutParams(params);

        AdManager.loadBanner(this, adContainer, "ca-app-pub-6598765502914364/9402654479");
        return adContainer;
    }

    private View.OnClickListener openSettingsActivity(String fragmentTag) {
        return v -> {
            Intent intent = new Intent(v.getContext(), SettingsActivity.class);
            intent.putExtra(SettingsActivity.FRAGMENT_TAG_EXTRA, fragmentTag);
            v.getContext().startActivity(intent);
        };
    }

    private LibraryItemView createPreference(int icon, String title, String desc, View.OnClickListener listener) {
        LibraryItemView preference = new LibraryItemView(this);
        preference.enabled.setVisibility(View.GONE);
        preference.icon.setImageResource(icon);
        preference.title.setText(title);
        preference.description.setText(desc);
        preference.setOnClickListener(listener);
        return preference;
    }

    private void openWorkingDirectory() {
        FilePickerOptions options = new FilePickerOptions();
        options.setSelectionMode(SelectionMode.BOTH);
        options.setMultipleSelection(true);
        options.setTitle(getString(R.string.auto_str_0387));
        options.setInitialDirectory(getFilesDir().getParentFile().getAbsolutePath());

        FilePickerCallback callback = new FilePickerCallback() {
            @Override
            public void onFilesSelected(@NotNull List<? extends File> files) {
                boolean isDirectory = files.get(0).isDirectory();
                if (files.size() > 1 || isDirectory) {
                    new MaterialAlertDialogBuilder(AppSettings.this)
                            .setTitle(R.string.auto_str_0385)
                            .setSingleChoiceItems(new String[]{"Delete"}, -1, (actionDialog, which) -> {
                                new MaterialAlertDialogBuilder(AppSettings.this)
                                        .setTitle(getString(R.string.auto_hardcoded_delete) + (isDirectory ? getString(R.string.auto_hardcoded_folder) : getString(R.string.auto_hardcoded_file)) + "?")
                                        .setMessage(getString(R.string.auto_hardcoded_are_you_sure_delete_this) + (isDirectory ? getString(R.string.auto_hardcoded_folder) : getString(R.string.auto_hardcoded_file)) + getString(R.string.auto_hardcoded_action_cannot_be_undone))
                                        .setPositiveButton(R.string.common_word_delete, (deleteConfirmationDialog, pressedButton) -> {
                                            for (File file : files) {
                                                FileUtil.deleteFile(file.getAbsolutePath());
                                                deleteConfirmationDialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton(R.string.common_word_cancel, null)
                                        .show();
                                actionDialog.dismiss();
                            })
                            .show();
                } else {
                    new MaterialAlertDialogBuilder(AppSettings.this)
                            .setTitle(R.string.auto_str_0385)
                            .setSingleChoiceItems(new String[]{"Edit", "Delete"}, -1, (actionDialog, which) -> {
                                switch (which) {
                                    case 0 -> {
                                        Intent intent = new Intent(getApplicationContext(), SrcCodeEditor.class);
                                        intent.putExtra("title", Uri.fromFile(files.get(0)).getLastPathSegment());
                                        intent.putExtra("content", files.get(0).getAbsolutePath());
                                        intent.putExtra("xml", "");
                                        startActivity(intent);
                                    }
                                    case 1 -> new MaterialAlertDialogBuilder(AppSettings.this)
                                            .setTitle(R.string.auto_str_0134)
                                            .setMessage(R.string.auto_str_0044)
                                            .setPositiveButton(R.string.common_word_delete, (deleteDialog, pressedButton) ->
                                                    FileUtil.deleteFile(files.get(0).getAbsolutePath()))
                                            .setNegativeButton(R.string.common_word_cancel, null)
                                            .show();
                                }
                                actionDialog.dismiss();
                            })
                            .show();
                }
            }
        };

        new FilePickerDialogFragment(options, callback).show(getSupportFragmentManager(), "file_picker");
    }

    private void signApkFileDialog() {
        boolean[] isAPKSelected = {false};
        MaterialAlertDialogBuilder apkPathDialog = new MaterialAlertDialogBuilder(this);
        apkPathDialog.setTitle(R.string.auto_str_0405);

        DialogSelectApkToSignBinding binding = DialogSelectApkToSignBinding.inflate(getLayoutInflater());
        View testkey_root = binding.getRoot();
        TextView apk_path_txt = binding.apkPathTxt;

        binding.selectFile.setOnClickListener(v -> {
            FilePickerOptions options = new FilePickerOptions();
            options.setExtensions(new String[]{"apk"});
            FilePickerCallback callback = new FilePickerCallback() {
                @Override
                public void onFileSelected(File file) {
                    isAPKSelected[0] = true;
                    apk_path_txt.setText(file.getAbsolutePath());
                }
            };
            FilePickerDialogFragment dialog = new FilePickerDialogFragment(options, callback);
            dialog.show(getSupportFragmentManager(), "file_picker");
        });

        apkPathDialog.setPositiveButton(R.string.auto_button_continue, (v, which) -> {
            if (!isAPKSelected[0]) {
                SketchwareUtil.toast("Please select an APK file to sign", Toast.LENGTH_SHORT);
                shakeView(binding.selectFile);
                return;
            }
            String input_apk_path = Helper.getText(apk_path_txt);
            String output_apk_file_name = Uri.fromFile(new File(input_apk_path)).getLastPathSegment();
            String output_apk_path = new File(Environment.getExternalStorageDirectory(),
                    "sketchware/signed_apk/" + output_apk_file_name).getAbsolutePath();

            if (new File(output_apk_path).exists()) {
                MaterialAlertDialogBuilder confirmOverwrite = new MaterialAlertDialogBuilder(this);
                confirmOverwrite.setIcon(R.drawable.color_save_as_new_96);
                confirmOverwrite.setTitle(R.string.auto_str_0203);
                confirmOverwrite.setMessage(getString(R.string.auto_hardcoded_apk_already_exists) + output_apk_file_name + getString(R.string.auto_hardcoded_apk_already_exists2));

                confirmOverwrite.setNegativeButton(Helper.getResString(R.string.common_word_cancel), null);
                confirmOverwrite.setPositiveButton(R.string.auto_button_overwrite, (view, which1) -> {
                    v.dismiss();
                    signApkFileWithDialog(input_apk_path, output_apk_path, true,
                            null, null, null, null);
                });
                confirmOverwrite.show();
            } else {
                signApkFileWithDialog(input_apk_path, output_apk_path, true,
                        null, null, null, null);
            }
        });

        apkPathDialog.setNegativeButton(Helper.getResString(R.string.common_word_cancel), null);

        apkPathDialog.setView(testkey_root);
        apkPathDialog.setCancelable(false);
        apkPathDialog.show();
    }

    private void signApkFileWithDialog(String inputApkPath, String outputApkPath, boolean useTestkey, String keyStorePath, String keyStorePassword, String keyStoreKeyAlias, String keyPassword) {
        View building_root = getLayoutInflater().inflate(R.layout.build_progress_msg_box, null, false);
        LinearLayout layout_quiz = building_root.findViewById(R.id.layout_quiz);
        TextView tv_progress = building_root.findViewById(R.id.tv_progress);

        ScrollView scroll_view = new ScrollView(this);
        TextView tv_log = new TextView(this);
        scroll_view.addView(tv_log);
        layout_quiz.addView(scroll_view);

        tv_progress.setText(R.string.auto_str_0406);

        AlertDialog building_dialog = new MaterialAlertDialogBuilder(this)
                .setView(building_root)
                .create();

        ApkSigner signer = new ApkSigner();
        new Thread() {
            @Override
            public void run() {
                super.run();

                ApkSigner.LogCallback callback = line -> runOnUiThread(() ->
                        tv_log.setText(Helper.getText(tv_log) + line));

                if (useTestkey) {
                    signer.signWithTestKey(inputApkPath, outputApkPath, callback);
                } else {
                    signer.signWithKeyStore(inputApkPath, outputApkPath,
                            keyStorePath, keyStorePassword, keyStoreKeyAlias, keyPassword, callback);
                }

                runOnUiThread(() -> {
                    if (ApkSigner.LogCallback.errorCount.get() == 0) {
                        building_dialog.dismiss();
                        SketchwareUtil.toast("Successfully saved signed APK to: /Internal storage/sketchware/signed_apk/"
                                        + Uri.fromFile(new File(outputApkPath)).getLastPathSegment(),
                                Toast.LENGTH_LONG);
                    } else {
                        tv_progress.setText(R.string.auto_str_0033);
                    }
                });
            }
        }.start();

        building_dialog.show();
    }

    private class ActivityLauncher implements View.OnClickListener {
        private final Intent launchIntent;
        private Pair<String, String> optionalExtra;

        ActivityLauncher(Intent launchIntent) {
            this.launchIntent = launchIntent;
        }

        ActivityLauncher(Intent launchIntent, Pair<String, String> optionalExtra) {
            this(launchIntent);
            this.optionalExtra = optionalExtra;
        }

        @Override
        public void onClick(View v) {
            if (optionalExtra != null) {
                launchIntent.putExtra(optionalExtra.first, optionalExtra.second);
            }
            startActivity(launchIntent);
        }
    }
}
