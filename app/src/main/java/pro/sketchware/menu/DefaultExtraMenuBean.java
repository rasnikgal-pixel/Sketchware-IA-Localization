package pro.sketchware.menu;

import pro.sketchware.R;

import android.net.Uri;
import android.util.Pair;

import com.besome.sketch.beans.ComponentBean;
import com.besome.sketch.editor.LogicEditorActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import a.a.a.Ss;
import a.a.a.eC;
import a.a.a.jC;
import a.a.a.uq;
import a.a.a.wq;
import mod.agus.jcoderz.editor.manage.block.makeblock.BlockMenu;
import mod.hey.studios.util.Helper;
import pro.sketchware.utility.CustomVariableUtil;
import pro.sketchware.utility.FileUtil;

public class DefaultExtraMenuBean {

    private final LogicEditorActivity logicEditor;
    private final eC projectDataManager;
    private final String sc_id;

    public DefaultExtraMenuBean(LogicEditorActivity logicEditor) {
        this.logicEditor = logicEditor;
        sc_id = logicEditor.scId;
        projectDataManager = jC.a(sc_id);
    }

    public static String getName(String menuName) {
        return switch (menuName) {
            case "image" -> "Custom Image";
            case "til_box_mode" -> "Box Mode";
            case "fabsize" -> "Fab Size";
            case "fabvisible" -> "Fab Visible";
            case "menuaction" -> "Menu Action";
            case "porterduff" -> "Porterduff Mode";
            case "transcriptmode" -> "Transcript Mode";
            case "listscrollparam", "recyclerscrollparam", "pagerscrollparam" -> "Scroll Param";
            case "gridstretchmode" -> "Stretch Mode";
            case "gravity_v" -> "Gravity Vertical";
            case "gravity_h" -> "Gravity Horizontal";
            case "gravity_t" -> "Gravity Toast";
            case "patternviewmode" -> "Pattern Mode";
            case "styleprogress" -> "Progress Style";
            case "cv_theme" -> "Theme";
            case "cv_language" -> "Language";
            case "import" -> "Import";
            default -> menuName;
        };
    }

    public Pair<String, ArrayList<String>> getMenu(Ss menu) {
        var javaName = logicEditor.M.getJavaName();
        var menuName = menu.getMenuName();
        ArrayList<String> menus = new ArrayList<>();
        String title;
        Pair<String, String[]> menuPair = BlockMenu.getMenu(menuName);
        title = menuPair.first;
        menus = new ArrayList<>(Arrays.asList(menuPair.second));
        for (String s : projectDataManager.e(javaName, 5)) {
            Matcher matcher2 = Pattern.compile("^(\\w+)[\\s]+(\\w+)").matcher(s);
            while (matcher2.find()) {
                if (menuName.equals(matcher2.group(1))) {
                    title = logicEditor.getString(R.string.auto_extra_menu_select_a) + matcher2.group(1) + logicEditor.getString(R.string.auto_extra_menu_select_var_suffix);
                    menus.add(matcher2.group(2));
                }
            }
        }
        for (String variable : projectDataManager.e(javaName, 6)) {
            String variableType = CustomVariableUtil.getVariableType(variable);
            String variableName = CustomVariableUtil.getVariableName(variable);
            if (menuName.equals(variableType)) {
                title = logicEditor.getString(R.string.auto_extra_menu_select_a) + variableType + logicEditor.getString(R.string.auto_extra_menu_select_var_suffix);
                menus.add(variableName);
            }
        }
        for (ComponentBean componentBean : projectDataManager.e(javaName)) {
            if (componentBean.type > 36
                    && menuName.equals(ComponentBean.getComponentTypeName(componentBean.type))) {
                title = logicEditor.getString(R.string.auto_extra_menu_select_a) + ComponentBean.getComponentTypeName(componentBean.type);
                menus.add(componentBean.componentId);
            }
        }
        switch (menuName) {
            case "LayoutParam" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_layout_params);
                menus.addAll(Helper.createStringList("MATCH_PARENT", "WRAP_CONTENT"));
            }
            case "Command" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_command);
                menus.addAll(
                        Helper.createStringList(
                                "insert",
                                "add",
                                "replace",
                                "find-replace",
                                "find-replace-first",
                                "find-replace-all"));
            }
            // This is meant to be a built-in menu including the cases below, but Aldi implemented it as a file, which is why, in some cases, certain menus appear empty.
            //start
            case "menu", "layout", "anim", "drawable" -> {
                String path = getPath(sc_id, menuName);
                title = logicEditor.getString(R.string.auto_extra_menu_select_a_menu) + menuName;
                if (menuName.equals("layout")) {
                    for (String name : jC.b(sc_id).e()) {
                        menus.add(name.substring(0, name.indexOf(".xml")));
                    }
                }
                for (String file : FileUtil.listFiles(path, ".xml")) {
                    menus.add(getFilename(file, ".xml"));
                }
            }
            case "image" -> {
                String path = getPath(sc_id, "drawable-xhdpi");
                title = logicEditor.getString(R.string.auto_extra_menu_select_image);
                for (String drawable_xhdpi : FileUtil.listFiles(path, "")) {
                    if (drawable_xhdpi.contains(".png") || drawable_xhdpi.contains(".jpg")) {
                        menus.add(
                                getFilename(
                                        drawable_xhdpi,
                                        drawable_xhdpi.contains(".png") ? ".png" : ".jpg"));
                    }
                }
            }
            case "til_box_mode" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_box_mode);
                menus.addAll(Arrays.asList(uq.TIL_BOX_MODE));
            }
            case "fabsize" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_fab_size);
                menus.addAll(Arrays.asList(uq.FAB_SIZE));
            }
            case "fabvisible" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_fab_visibility);
                menus.addAll(Arrays.asList(uq.FAB_VISIBLE));
            }
            case "menuaction" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_menu_action);
                menus.addAll(Arrays.asList(uq.MENU_ACTION));
            }
            case "porterduff" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_porterduff_mode);
                menus.addAll(Arrays.asList(uq.PORTER_DUFF));
            }
            case "transcriptmode" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_transcript_mode);
                menus.addAll(Arrays.asList(uq.TRANSCRIPT_MODE));
            }
            // idk, but it seems this isn't used anywhere, yet it was included in the menu file.
            case "listscrollparam" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_scroll_param);
                menus.addAll(Arrays.asList(uq.LIST_SCROLL_STATES));
            }
            // same with listscrollparam
            case "recyclerscrollparam", "pagerscrollparam" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_scroll_param);
                menus.addAll(Arrays.asList(uq.RECYCLER_SCROLL_STATES));
            }
            case "gridstretchmode" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_stretch_mode);
                menus.addAll(Arrays.asList(uq.GRID_STRETCH_MODE));
            }
            case "gravity_v" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_gravity_vertical);
                menus.addAll(Arrays.asList(uq.GRAVITY_VERTICAL));
            }
            case "gravity_h" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_gravity_horizontal);
                menus.addAll(Arrays.asList(uq.GRAVITY_HORIZONTAL));
            }
            case "gravity_t" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_gravity_toast);
                menus.addAll(Arrays.asList(uq.GRAVITY_TOAST));
            }
            case "patternviewmode" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_patternview_mode);
                menus.addAll(Arrays.asList(uq.PATTERNVIEW_MODE));
            }
            case "styleprogress" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_progress_style);
                menus.addAll(Arrays.asList(uq.PROGRESS_STYLE));
            }
            case "cv_theme" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_theme);
                menus.addAll(Arrays.asList(uq.CODEVIEW_THEME));
            }
            case "cv_language" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_language);
                menus.addAll(Arrays.asList(uq.CODEVIEW_LANGUAGE));
            }
            case "import" -> {
                title = logicEditor.getString(R.string.auto_extra_menu_select_language);
                menus.addAll(Arrays.asList(uq.IMPORT_CLASS_PATH));
            }
            //end
        }
        return new Pair<>(title, menus);
    }

    private String getPath(String sc_id, String name) {
        return wq.b(sc_id) + "/files/resource/" + name + "/";
    }

    private String getFilename(String filePath, String filenameExtensionToCutOff) {
        String lastPathSegment = Uri.parse(filePath).getLastPathSegment();
        return lastPathSegment.substring(0, lastPathSegment.indexOf(filenameExtensionToCutOff));
    }
}
