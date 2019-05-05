package peter.azzie;

import org.junit.Test;

import peter.azzie.io.DataLayer;
import peter.azzie.io.memory.MemoryDataLayer;

import static org.assertj.core.api.Assertions.*;

public class MemoryDataLayerTest {

    @Test public void canAccessFilesInCurrentDir(){
        assertThatCode(() -> createDataLayer().getStorage("users.txt"))
                .doesNotThrowAnyException();
    }

    @Test public void subdirectoriesNeedToBeCreated(){
        assertThatThrownBy(() -> createDataLayer().getStorage("john/settings.txt"))
                .hasMessage("directory does not exist");
    }

    @Test
    public void canCreateSubdirectoreis(){
        assertThatCode(() -> {
            DataLayer dataLayer = createDataLayer();
            dataLayer.setupDirectory("john");
            dataLayer.getStorage("john/settings.txt");
        }).doesNotThrowAnyException();
    }

    @Test
    public void canCreateSubdirectoriesEndingWithSlash(){
        assertThatCode(() -> {
            DataLayer dataLayer = createDataLayer();
            dataLayer.setupDirectory("john/");
            dataLayer.getStorage("john/settings.txt");
        }).doesNotThrowAnyException();
    }

    @Test public void canMakePaths(){
        assertThat(createDataLayer().makePath("john","settings.txt"))
                .isEqualTo("john/settings.txt");
    }

    private DataLayer createDataLayer() {
        MemoryDataLayer fileDataLayer = new MemoryDataLayer();
        return fileDataLayer;
    }
}
