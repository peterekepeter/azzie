package peter.azzie;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

import peter.azzie.io.DataLayer;
import peter.azzie.io.memory.MemoryDataLayer;

public class FileSettingsTest {

    @Test public void canCreate(){
        assertThatCode(() -> makeFileSettings()).doesNotThrowAnyException();
    }

    @Test public void readingEmptySettingsReturnsNull(){
        assertThat(makeFileSettings().getString("user")).isNull();
    }

    @Test public void canWriteSettings(){
        FileSettings settings = makeFileSettings();
        settings.setString("color", "blue");
        assertThat(settings.getString("color")).isEqualTo("blue");
    }

    @Test public void dataLayerEnsuresPersistance(){
        MemoryDataLayer dataLayer = new MemoryDataLayer();
        FileSettings settings = new FileSettings(dataLayer.getStorage("settings.txt"));
        settings.setString("color", "blue");
        settings = new FileSettings(dataLayer.getStorage("settings.txt"));
        assertThat(settings.getString("color")).isEqualTo("blue");
    }

    @Test public void differentPathsStoreDifferentSettings(){
        FileSettings[] users = makeMultiple("user1.txt", "user2.txt");
        FileSettings user1 = users[0];
        FileSettings user2 = users[1];
        user1.setString("color", "blue");
        assertThat(user2.getString("color")).isNotEqualTo("blue");
        user2.setString("color", "red");
        assertThat(user1.getString("color")).isEqualTo("blue");
    }

    @Test public void canStoreMultipleValues(){
        FileSettings settings = makeFileSettings();
        settings.setString("name", "John");
        settings.setString("age", "42");
        assertThat(settings.getString("name")).isEqualTo("John");
        assertThat(settings.getString("age")).isEqualTo("42");
    }

    private DataLayer makeDataLayer(){
        return new MemoryDataLayer();
    }

    private FileSettings makeFileSettings(){
        return new FileSettings(makeDataLayer().getStorage("settings.txt"));
    }

    private FileSettings[] makeMultiple(String... paths){
        DataLayer dataLayer = makeDataLayer();
        FileSettings[] result = new FileSettings[paths.length];
        for (int i=0; i<result.length; i++){
            result[i] = new FileSettings(dataLayer.getStorage(paths[i]));
        }
        return result;
    }

}