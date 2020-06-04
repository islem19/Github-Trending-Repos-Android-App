package dz.islem.githubapi.ui.home;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dz.islem.githubapi.RxImmediateScheduler;
import dz.islem.githubapi.Utils;
import dz.islem.githubapi.data.DataManager;
import dz.islem.githubapi.data.model.ItemModel;
import dz.islem.githubapi.data.model.RepoModel;
import io.reactivex.Observable;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MainViewModelTest {

    @Rule public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Rule public RxImmediateScheduler rxImmediateScheduler = new RxImmediateScheduler();
    @Mock
    DataManager dataManager;
    private MainViewModel viewModel;
    private Map<String, String> map = new HashMap<>();
    @Mock
    Observer<List<ItemModel>> dataObserver;
    @Mock
    Observer<Boolean> errorObserver;
    @Captor
    ArgumentCaptor<List<ItemModel>> arg;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        viewModel = new MainViewModel(dataManager);
        viewModel.getRepos().observeForever(dataObserver);
        viewModel.getError().observeForever(errorObserver);
    }

    @Test
    public void testNull(){
        when(dataManager.getRepos(anyMap())).thenReturn(null);
        assertNotNull(viewModel.getRepos());
        assertTrue(viewModel.getRepos().hasActiveObservers());
        assertTrue(viewModel.getError().hasObservers());
    }

    @Test
    public void testNewData(){
        List<ItemModel> items = Utils.generateTestOneItemModel();
        RepoModel repo = Utils.generateRepo(items);

        //given
        given(dataManager.getRepos(anyMap())).willReturn(Observable.just(repo));
        //act
        viewModel.loadRepos(anyString());
        //verify
        then(dataObserver).should(times(1)).onChanged(items);
        then(errorObserver).should(times(1)).onChanged(false);

    }

    @Test
    public void testVerifyRightData(){
        List<ItemModel> testOneItemModel = Utils.generateTestOneItemModel();
        RepoModel repo = Utils.generateRepo(testOneItemModel);

        //given
        given(dataManager.getRepos(initMap("2020-10-20"))).willReturn(Observable.just(repo));
        //act
        viewModel.loadRepos("2020-10-20");
        //verify
        then(dataObserver).should(times(1)).onChanged(arg.capture());

        List<ItemModel> capturedItems = arg.getAllValues().get(0);
        assertEquals(capturedItems.get(0), testOneItemModel.get(0));
    }


    @Test
    public void testErrorLocation(){
        //given
        Throwable error = new Throwable("Error Response");
        given(dataManager.getRepos(anyMap()))
                .willReturn(Observable.error(error));
        // when
        viewModel.loadRepos(anyString());
        //then
        then(errorObserver).should(times(1)).onChanged(true);
        then(dataObserver).should(times(0)).onChanged(null);
    }

    @After
    public void tearDown(){
        dataManager = null;
    }

    private Map<String, String> initMap(String date){
        map.put("q","created:>"+date);
        map.put("sort","stars");
        map.put("order","desc");
        map.put("page","1");
        return map;
    }




}