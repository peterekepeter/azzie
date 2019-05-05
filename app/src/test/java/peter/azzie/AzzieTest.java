package peter.azzie;

import org.junit.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.*;

import peter.azzie.io.DataLayer;
import peter.azzie.io.memory.MemoryDataLayer;

public class AzzieTest {

    @Test public void canCreateAzzie(){
        assertThatCode(() -> createAzzie()).doesNotThrowAnyException();
    }

    @Test public void hasDatasetsOnFirstRun(){
        String[] datasets = createAzzie().getDatasets();
        assertThat(datasets).isNotEmpty();
    }

    @Test public void hasSuggetionsOnFirstRun(){
        Azzie azzie = createAzzie();
        assertThat(azzie.getSuggestedActivities()).isNotEmpty();
    }

    @Test public void canStartActivity(){
        Azzie azzie = createAzzie();
        String activityName = azzie.getSuggestedActivities()[0];
        azzie.setCurrentActivity(activityName);
        assertThat(azzie.getCurrentActivity().activity).isEqualTo(activityName);
    }

    @Test public void hasNoActivitySetOnFirstRun(){
        Azzie azzie = createAzzie();
        Azzie.CurrentActivityInfo info = azzie.getCurrentActivity();
        assertThat(info.event).isNull();
        assertThat(info.activity).isNotNull();
        assertThat(info.timeExpression).isNotNull();
    }

    @Test public void activitySuggestionIsBasedOnPastActivities(){
        Azzie azzie = createAzzie();
        azzie.setCurrentActivity("sleeping");
        azzie.setCurrentActivity("eating");
        azzie.setCurrentActivity("writing some code");
        azzie.setCurrentActivity("working");
        assertThat(azzie.getSuggestedActivities())
                .contains("sleeping", "eating", "writing some code");
    }

    private Azzie createAzzie(){
        DataLayer dataLayer = new MemoryDataLayer();
        Azzie azzie = new Azzie(dataLayer);
        return azzie;
    }

}
