package peter.azzie;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

import peter.azzie.io.DataLayer;
import peter.azzie.io.memory.MemoryDataLayer;

public class SettingsStorageTest {

    @Test public void canCreate(){
        assertThatCode(() -> makeFileSettings()).doesNotThrowAnyException();
    }

    @Test public void readingEmptySettingsReturnsNull(){
        assertThat(makeFileSettings().getString("user")).isNull();
    }

    @Test public void canWriteSettings(){
        SettingsStorage settings = makeFileSettings();
        settings.setString("color", "blue");
        assertThat(settings.getString("color")).isEqualTo("blue");
    }

    @Test public void dataLayerEnsuresPersistance(){
        MemoryDataLayer dataLayer = new MemoryDataLayer();
        SettingsStorage settings = new SettingsStorage(dataLayer.getStorage("settings.txt"));
        settings.setString("color", "blue");
        settings = new SettingsStorage(dataLayer.getStorage("settings.txt"));
        assertThat(settings.getString("color")).isEqualTo("blue");
    }

    @Test public void differentPathsStoreDifferentSettings(){
        SettingsStorage[] users = makeMultiple("user1.txt", "user2.txt");
        SettingsStorage user1 = users[0];
        SettingsStorage user2 = users[1];
        user1.setString("color", "blue");
        assertThat(user2.getString("color")).isNotEqualTo("blue");
        user2.setString("color", "red");
        assertThat(user1.getString("color")).isEqualTo("blue");
    }

    @Test public void canStoreMultipleValues(){
        SettingsStorage settings = makeFileSettings();
        settings.setString("name", "John");
        settings.setString("age", "42");
        assertThat(settings.getString("name")).isEqualTo("John");
        assertThat(settings.getString("age")).isEqualTo("42");
    }

    private DataLayer makeDataLayer(){
        return new MemoryDataLayer();
    }

    private SettingsStorage makeFileSettings(){
        return new SettingsStorage(makeDataLayer().getStorage("settings.txt"));
    }

    private SettingsStorage[] makeMultiple(String... paths){
        DataLayer dataLayer = makeDataLayer();
        SettingsStorage[] result = new SettingsStorage[paths.length];
        for (int i=0; i<result.length; i++){
            result[i] = new SettingsStorage(dataLayer.getStorage(paths[i]));
        }
        return result;
    }

}