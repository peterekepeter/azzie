package peter.azzie;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

import peter.azzie.io.DataLayer;
import peter.azzie.io.memory.MemoryDataLayer;

public class SettingsStorageTest {

    @Test public void canCreate(){
        assertThatCode(() -> makeSettingsStorage()).doesNotThrowAnyException();
    }

    @Test public void readingEmptySettingsReturnsNull(){
        assertThat(makeSettingsStorage().getString("user")).isNull();
    }

    @Test public void canWriteSettings(){
        SettingsStorage settings = makeSettingsStorage();
        settings.setString("color", "blue");
        settings.saveChanges();
        assertThat(settings.getString("color")).isEqualTo("blue");
    }

    @Test public void dataLayerEnsuresPersistance(){
        MemoryDataLayer dataLayer = new MemoryDataLayer();
        SettingsStorage settings = new SettingsStorage(dataLayer.getStorage("settings.txt"));
        settings.setString("color", "blue");
        settings.saveChanges();
        settings = new SettingsStorage(dataLayer.getStorage("settings.txt"));
        assertThat(settings.getString("color")).isEqualTo("blue");
    }

    @Test public void differentPathsStoreDifferentSettings(){
        SettingsStorage[] users = makeMultiple("user1.txt", "user2.txt");
        SettingsStorage user1 = users[0];
        SettingsStorage user2 = users[1];
        user1.setString("color", "blue");
        user1.saveChanges();
        assertThat(user2.getString("color")).isNotEqualTo("blue");
        user2.setString("color", "red");
        user2.saveChanges();
        assertThat(user1.getString("color")).isEqualTo("blue");
    }

    @Test public void canStoreMultipleValues(){
        SettingsStorage settings = makeSettingsStorage();
        settings.setString("name", "John");
        settings.setString("age", "42");
        settings.saveChanges();
        assertThat(settings.getString("name")).isEqualTo("John");
        assertThat(settings.getString("age")).isEqualTo("42");
    }

    @Test public void hasChangesIsFalseAtStart(){
        SettingsStorage settings = makeSettingsStorage();
        assertThat(settings.hasChanges()).isFalse();
    }

    @Test public void hasChangesIsFalseAfterGettingString(){
        SettingsStorage settings = makeSettingsStorage();
        settings.getString("key");
        assertThat(settings.hasChanges()).isFalse();
    }

    @Test public void hasChangesIsTrueAfterSettingAString(){
        SettingsStorage settings = makeSettingsStorage();
        settings.setString("key", "value");
        assertThat(settings.hasChanges()).isTrue();
    }

    @Test public void hasChangesIsFalseAfterSavingToStorage(){
        SettingsStorage settings = makeSettingsStorage();
        settings.setString("key", "value");
        assertThat(settings.hasChanges()).isTrue();
        settings.saveChanges();
        assertThat(settings.hasChanges()).isFalse();
    }

    private DataLayer makeDataLayer(){
        return new MemoryDataLayer();
    }

    private SettingsStorage makeSettingsStorage(){
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