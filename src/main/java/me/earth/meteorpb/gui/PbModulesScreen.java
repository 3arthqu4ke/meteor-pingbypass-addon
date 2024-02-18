package me.earth.meteorpb.gui;

import me.earth.meteorpb.module.TranslatedModule;
import me.earth.meteorpb.util.ValueComparableMapWithCustomKeys;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.module.Category;
import me.earth.pingbypass.api.module.Module;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.traits.Nameable;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.TabScreen;
import meteordevelopment.meteorclient.gui.utils.Cell;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.gui.widgets.containers.WSection;
import meteordevelopment.meteorclient.gui.widgets.containers.WVerticalList;
import meteordevelopment.meteorclient.gui.widgets.containers.WWindow;
import meteordevelopment.meteorclient.gui.widgets.input.WTextBox;
import meteordevelopment.meteorclient.systems.config.Config;
import meteordevelopment.meteorclient.utils.Utils;
import net.minecraft.item.Items;

import java.util.*;

import static meteordevelopment.meteorclient.utils.Utils.getWindowHeight;
import static meteordevelopment.meteorclient.utils.Utils.getWindowWidth;

/**
 * {@link meteordevelopment.meteorclient.gui.screens.ModulesScreen}
 */
public class PbModulesScreen extends TabScreen {
    private final PingBypass pingBypass;
    private WCategoryController controller;

    public PbModulesScreen(PingBypass pingBypass, Tab tab, GuiTheme theme) {
        super(theme, tab);
        this.pingBypass = pingBypass;
    }

    @Override
    public void initWidgets() {
        controller = add(new WCategoryController()).widget();

        // Help
        WVerticalList help = add(theme.verticalList()).pad(4).bottom().widget();
        help.add(theme.label("Left click - Toggle module"));
        help.add(theme.label("Right click - Open module settings"));
    }

    @Override
    protected void init() {
        super.init();
        controller.refresh();
    }

    private WWidget module(Module module) {
        return theme.module(new TranslatedModule(module));
    }

    // Category

    protected WWindow createCategory(WContainer c, Category category) {
        meteordevelopment.meteorclient.systems.modules.Category meteorCategory = TranslatedModule.fromPingBypassCategory(category);

        WWindow w = theme.window(category.getName());
        w.id = "PingBypass" + pingBypass.getSide().getName() + "-" + category.getName();
        w.padding = 0;
        w.spacing = 0;

        if (theme.categoryIcons()) {
            w.beforeHeaderInit = wContainer -> wContainer.add(theme.item(meteorCategory.icon)).pad(2);
        }

        c.add(w);
        w.view.scrollOnlyWhenMouseOver = true;
        w.view.hasScrollBar = false;
        w.view.spacing = 0;

        pingBypass.getModuleManager().getModulesByCategory(category).forEach(module -> {
            w.add(module(module)).expandX();
        });

        return w;
    }

    protected void createSearchW(WContainer w, String text) {
        if (!text.isEmpty()) {
            // Titles
            Set<Module> modules = searchTitles(text);

            if (modules.size() > 0) {
                WSection section = w.add(theme.section("Modules")).expandX().widget();
                section.spacing = 0;

                int count = 0;
                for (Module module : modules) {
                    if (count >= Config.get().moduleSearchCount.get() || count >= modules.size()) break;
                    section.add(module(module)).expandX();
                    count++;
                }
            }

            // Settings
            modules = searchSettingTitles(text);

            if (modules.size() > 0) {
                WSection section = w.add(theme.section("Settings")).expandX().widget();
                section.spacing = 0;

                int count = 0;
                for (Module module : modules) {
                    if (count >= Config.get().moduleSearchCount.get() || count >= modules.size()) break;
                    section.add(module(module)).expandX();
                    count++;
                }
            }
        }
    }

    protected WWindow createSearch(WContainer c) {
        WWindow w = theme.window("Search");
        w.id = "search";

        if (theme.categoryIcons()) {
            w.beforeHeaderInit = wContainer -> wContainer.add(theme.item(Items.COMPASS.getDefaultStack())).pad(2);
        }

        c.add(w);
        w.view.scrollOnlyWhenMouseOver = true;
        w.view.hasScrollBar = false;
        w.view.maxHeight -= 20;

        WVerticalList l = theme.verticalList();

        WTextBox text = w.add(theme.textBox("")).minWidth(140).expandX().widget();
        text.setFocused(true);
        text.action = () -> {
            l.clear();
            createSearchW(l, text.get());
        };

        w.add(l).expandX();
        createSearchW(l, text.get());

        return w;
    }

    // Favorites

    protected Cell<WWindow> createFavorites(WContainer c) {
        // TODO!
        boolean hasFavorites = false;
        if (!hasFavorites) return null;

        WWindow w = theme.window("Favorites");
        w.id = "favorites";
        w.padding = 0;
        w.spacing = 0;

        if (theme.categoryIcons()) {
            w.beforeHeaderInit = wContainer -> wContainer.add(theme.item(Items.NETHER_STAR.getDefaultStack())).pad(2);
        }

        Cell<WWindow> cell = c.add(w);
        w.view.scrollOnlyWhenMouseOver = true;
        w.view.hasScrollBar = false;
        w.view.spacing = 0;

        createFavoritesW(w);
        return cell;
    }

    protected boolean createFavoritesW(WWindow w) {
        List<Module> modules = new ArrayList<>();

        for (Module module : pingBypass.getModuleManager()) {
            if (false) { // TODO
                modules.add(module);
            }
        }

        modules.sort(Nameable::compareAlphabetically);
        for (Module module : modules) {
            w.add(module(module)).expandX();
        }

        return !modules.isEmpty();
    }

    @Override
    public boolean toClipboard() {
        // TODO return NbtUtils.toClipboard(Modules.get());
        return false;
    }

    @Override
    public boolean fromClipboard() {
        // TODO return NbtUtils.fromClipboard(Modules.get());
        return false;
    }

    @Override
    public void reload() {
    }

    /**
     * @see meteordevelopment.meteorclient.systems.modules.Modules#searchTitles(String)
     */
    public Set<Module> searchTitles(String text) {
        Map<Module, Integer> modules = new ValueComparableMapWithCustomKeys<>(Comparator.naturalOrder(), Nameable::compareAlphabetically);

        for (Module module : pingBypass.getModuleManager()) {
            int score = Utils.searchLevenshteinDefault(module.getName(), text, false);
            modules.put(module, modules.getOrDefault(module, 0) + score);
        }

        return modules.keySet();
    }

    /**
     * @see meteordevelopment.meteorclient.systems.modules.Modules#searchSettingTitles(String)
     */
    public Set<Module> searchSettingTitles(String text) {
        Map<Module, Integer> modules = new ValueComparableMapWithCustomKeys<>(Comparator.naturalOrder(), Nameable::compareAlphabetically);

        for (Module module : pingBypass.getModuleManager()) {
            int lowest = Integer.MAX_VALUE;
            for (Setting<?> setting : module) {
                int score = Utils.searchLevenshteinDefault(setting.getName(), text, false);
                if (score < lowest) lowest = score;
            }

            modules.put(module, modules.getOrDefault(module, 0) + lowest);
        }

        return modules.keySet();
    }

    // Stuff

    protected class WCategoryController extends WContainer {
        public final List<WWindow> windows = new ArrayList<>();
        private Cell<WWindow> favorites;

        @Override
        public void init() {
            for (Category category : pingBypass.getModuleManager().getCategoryManager()) {
                windows.add(createCategory(this, category));
            }

            windows.add(createSearch(this));

            refresh();
        }

        protected void refresh() {
            if (favorites == null) {
                favorites = createFavorites(this);
                if (favorites != null) windows.add(favorites.widget());
            } else {
                favorites.widget().clear();

                if (!createFavoritesW(favorites.widget())) {
                    remove(favorites);
                    windows.remove(favorites.widget());
                    favorites = null;
                }
            }
        }

        @Override
        protected void onCalculateWidgetPositions() {
            double pad = theme.scale(4);
            double h = theme.scale(40);

            double x = this.x + pad;
            double y = this.y;

            for (Cell<?> cell : cells) {
                double windowWidth = getWindowWidth();
                double windowHeight = getWindowHeight();

                if (x + cell.width > windowWidth) {
                    x = x + pad;
                    y += h;
                }

                if (x > windowWidth) {
                    x = windowWidth / 2.0 - cell.width / 2.0;
                    if (x < 0) x = 0;
                }
                if (y > windowHeight) {
                    y = windowHeight / 2.0 - cell.height / 2.0;
                    if (y < 0) y = 0;
                }

                cell.x = x;
                cell.y = y;

                cell.width = cell.widget().width;
                cell.height = cell.widget().height;

                cell.alignWidget();

                x += cell.width + pad;
            }
        }
    }
}
