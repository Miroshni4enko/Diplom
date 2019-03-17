package ua.sumy.sttp.diplom.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import ua.sumy.sttp.diplom.ImageManager;
import ua.sumy.sttp.diplom.R;
import ua.sumy.sttp.diplom.model.ModelDataBase;
import ua.sumy.sttp.diplom.model.MySQLDataAccess;
import ua.sumy.sttp.diplom.model.Posts;

/*import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;*/

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMain.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMain extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TITLE = "title";
    private static final String DATE = "date";
    private static final String IMGKEY = "iconfromraw";
    private TextView textView;
    private ImageView imageView;
    private ListView listView;
    private List<HashMap<String,Object>> mapList;
    List<Posts> listPosts;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragmentMain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMain.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMain newInstance(String param1, String param2) {
        FragmentMain fragment = new FragmentMain();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        /*textView = (TextView) getActivity().findViewById(R.id.posts);
        imageView = (ImageView) getActivity().findViewById(R.id.imagePost);
        */AtomicReference<Thread> myThready = new AtomicReference<Thread>(new Thread(new Runnable() { //подготовливаем отдельний поток
            public void run() //Этот метод будет выполняться в побочном потоке
            {
                ModelDataBase modelDataBase = new MySQLDataAccess();
                listPosts = modelDataBase.getAllPosts();
            }
        }));
        Thread thread =  myThready.get();
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        listView = (ListView) getActivity().findViewById(R.id.list_posts);
        mapList = new ArrayList<>();
        HashMap<String,Object> hm;
        for(Posts posts: listPosts){
            hm = new HashMap<String, Object>();
            hm.put(TITLE, posts.getTitle());
            hm.put(DATE, posts.getDate());
            /*ImageView imageView = (ImageView) getActivity().findViewById(R.id.img);
            imageView = ImageManager.dowloadImageWithThread(posts.getImage(),imageView);
            System.out.println(imageView);
            */
            Bitmap bitmap = ImageManager.downloadImage(posts.getImage());
            Drawable drawable = new BitmapDrawable(getResources(),bitmap);

            hm.put(IMGKEY, drawable);
            mapList.add(hm);
        }
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),
                mapList,
                R.layout.list_posts, new String[]{
                TITLE,         //верхний текст
                DATE,        //нижний теккт
                IMGKEY          //наша картинка
        }, new int[]{
                R.id.text1, //ссылка на объект отображающий текст
                R.id.text2, //ссылка на объект отображающий текст
                R.id.img}); //добавили ссылку в чем отображать картинки из list.xml

        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        textView.setText(listPosts.get(0).getShortStory() + listPosts.get(0).getImage()+ listPosts.get(1).getImage()+ listPosts.get(2).getImage());
        //ImageManager.fetchImage(listPosts.get(0).getImage(),imageView);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
