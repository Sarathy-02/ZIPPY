package sara.converter.util;

import static sara.converter.util.Constants.THEME_BLACK;
import static sara.converter.util.Constants.THEME_DARK;
import static sara.converter.util.Constants.THEME_SYSTEM;
import static sara.converter.util.Constants.THEME_WHITE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.preference.PreferenceManager;

import sara.converter.R;

public class ThemeUtils {

    public static ThemeUtils getInstance() {
        return ThemeUtils.SingletonHolder.INSTANCE;
    }

    /**
     * Set selected theme to current context
     *
     * @param context - current context
     */
    public void setThemeApp(Context context) {
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String themeName = mSharedPreferences.getString(Constants.DEFAULT_THEME_TEXT,
                Constants.DEFAULT_THEME);
        if (themeName == null)
            return;
        switch (themeName) {
            case THEME_WHITE -> context.setTheme(R.style.AppThemeWhite);
            case THEME_BLACK -> context.setTheme(R.style.AppThemeBlack);
            case THEME_DARK -> context.setTheme(R.style.ActivityThemeDark);
            default -> {
                if ((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
                    context.setTheme(R.style.ActivityThemeDark);
                } else {
                    context.setTheme(R.style.AppThemeWhite);
                }
            }
        }
    }

    /**
     * get position of selected theme
     *
     * @param context - current context
     * @return - position
     */
    public int getSelectedThemePosition(Context context) {
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String themeName = mSharedPreferences.getString(Constants.DEFAULT_THEME_TEXT,
                Constants.DEFAULT_THEME);
        return switch (themeName) {
            case THEME_SYSTEM -> 0;
            case THEME_BLACK -> 1;
            case THEME_DARK -> 2;
            case THEME_WHITE -> 3;
            default -> 0;
        };
    }

    /**
     * Save given theme to shared prefs
     *
     * @param context   - current context
     * @param themeName - name of theme to save
     */
    public void saveTheme(Context context, String themeName) {
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.DEFAULT_THEME_TEXT, themeName);
        editor.apply();
    }

    private static class SingletonHolder {
        static final ThemeUtils INSTANCE = new ThemeUtils();
    }
}
