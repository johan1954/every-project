package com.example.elokuvattv;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity<view> extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener {

    /* Määritellään muuttujat käyttöliittymään */
    private ImageButton kalenteriPainike;
    private ImageButton time1Painike, time2painike;
    private Spinner spinnerTeatteri;

    private RecyclerView recyclerElokuva;
    private RecyclerView.Adapter rAdapter;
    private RecyclerView.LayoutManager recyclerLayout;

    private EditText textPaiva;
    private EditText editTextMovie;
    private ProgressBar progress;

    /* Määritellään muut muuttujat: */
    String urlAreas = "https://www.finnkino.fi/xml/TheatreAreas/", urlMovies;
    teatteriList listaTeatterit;
    elokuvatList listaElokuvat;
    parseData parser;
    ArrayList<String> listaTeattereista;
    ArrayAdapter<String> adapter = null;
    ArrayList<recyclerItem> recyclerArray;
    String parsePaivamaara, currTeatteri, currentDateString;
    String time1, time2;
    int hour, minute, buttonclicked, currTheatreId = 1029, counter;

    String xmlTitle, xmlStartTime, xmlTheaterAndAutorium;
    int xmlTheatreId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* Luodaan uusi teatterit listaava olio: */
        listaTeatterit = teatteriList.getInstance();


        /* Asetetaan referenssit oikeisiin painikkeisiin ja tekstilaatikoihin. */
        textPaiva = (EditText) findViewById(R.id.textPaiva);
        thisDayIs();
        textPaiva.setText(currentDateString);
        parsePaivamaara = currentDateString;
        spinnerTeatteri = (Spinner) findViewById(R.id.spinnerTeatteri);
        recyclerElokuva = (RecyclerView) findViewById(R.id.recyclerElokuva);
        editTextMovie = (EditText) findViewById(R.id.movieFindEditText);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setMax(100);
        progress.setMin(0);


        /* Verkosta lukemiseen tarvittavat turvallisuus "policyt" */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /* Luetaan XML */
        StartAsyncFetch();

        /* Luodaan elokuvista alkiot listaan */
        parser = parseData.getInstance();
        listaElokuvat = elokuvatList.getInstance();

        /* Tehdään dialogi päivämäärän valinnalle... */
        kalenteriPainike = (ImageButton) findViewById(R.id.buttonCalendar);
        kalenteriPainike.setOnClickListener(new View.OnClickListener() {

            /* Painike kalenteri -dialogin avaamiseksi. */
            @Override
            public void onClick(View v) {
                DialogFragment pickADate = new datePicker();
                pickADate.show(getSupportFragmentManager(), "date picker");
            }
        });

        /* Tehdään dialogi kellonajan valinnalle... */
        time1Painike = (ImageButton) findViewById(R.id.time1butt);
        time2painike = (ImageButton) findViewById(R.id.time2butt);

        /* On click listener kellonajan valinnalle... */
        time1Painike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment time1picker = new timePicker();
                time1picker.show(getSupportFragmentManager(),"time picker");

                buttonclicked = 1;

            }
        });

        time2painike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment time1picker = new timePicker();
                time1picker.show(getSupportFragmentManager(),"time picker");
                buttonclicked = 2;
            }
        });
        /* Päivämäärän valinnan kuuntelija */

        textPaiva.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                parsePaivamaara = (String) textPaiva.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /* Etsimisen tarkistaja */
        editTextMovie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temporary = (String) editTextMovie.getText().toString();
                listaElokuvat.setFilterName(temporary);
                setElokuvatListaan();
            }
        });
    }

    /* New Date picker on date set */
    /* *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar kalenteri = Calendar.getInstance();
        /* Asetetaan parametreina saadut arvot kalenter -olioon. */
        kalenteri.set(Calendar.YEAR, year);
        kalenteri.set(Calendar.MONTH, month);
        kalenteri.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        /* Asetetaan kalenteri- olion arvot merkkijonoksi. */
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDateString = sdf.format(kalenteri.getTime()).toString();/* DateFormat.getDateInstance(DateFormat.LONG).format(kalenteri.getTime());*/
        parsePaivamaara = currentDateString;
        textPaiva.setText(currentDateString);
        uusiHaku();
        setElokuvatListaan();
//        System.out.println(currentDateString);
    }

    /* *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** */
    /* New Time picker on time set */
    /* *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** */

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {

        this.hour = hour;
        this.minute = minute;
        setTime();
        setElokuvatListaan();
    }

    /* *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** */
    /* Spinner teatteri */
    /* *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** */
    public void doSpinnerTeatteri() {
        listaTeattereista = new ArrayList<>();
        listaTeattereista = listaTeatterit.teatteritMerkkijonoiksi();
        /*Array Adapter: */
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_color, listaTeattereista);
        adapter.setDropDownViewResource(R.layout.spinner_item_color2);
        spinnerTeatteri.setAdapter(adapter);
        /* Item Selected */
        spinnerTeatteri.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currTeatteri = parent.getItemAtPosition(position).toString();
        currTheatreId = Integer.parseInt(listaTeatterit.getTheaterId(currTeatteri));
        uusiHaku();
        setElokuvatListaan();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    /* *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** */
    /* New urlFetch file */
    /* *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** */


    public String urlFetch(){
        String urlThis = "http://www.finnkino.fi/xml/Schedule/?area=";
        String temp = listaTeatterit.getTheaterId(currTeatteri);
        System.out.println(temp+ " "+listaTeatterit.getTheaterId(currTeatteri));
        if (!temp.equals("null")) {
            System.out.println(temp);
            urlThis = urlThis.concat(temp).concat("&dt=");
            System.out.println(urlThis);
        }
        else {
            return "";
        }
        urlThis = urlThis.concat(parsePaivamaara);
        return urlThis;
    }


    /* Make a fetch for all theaters! */
    public void fetchAsync() {
        if (currTheatreId == 1029) {
            if (counter == 9) {
                urlMovies = "http://www.finnkino.fi/xml/Schedule/?area=1014&dt=".concat(parsePaivamaara);
                StartAsyncFetchMovies();
                counter = counter-1;
            }
            else if (counter == 8) {
                urlMovies = "http://www.finnkino.fi/xml/Schedule/?area=1015&dt=".concat(parsePaivamaara);
                StartAsyncFetchMovies();
                counter = counter-1;
            }
            else if (counter == 7) {
                urlMovies = "http://www.finnkino.fi/xml/Schedule/?area=1016&dt=".concat(parsePaivamaara);
                StartAsyncFetchMovies();
                counter = counter-1;
            }
            else if (counter == 6) {
                urlMovies = "http://www.finnkino.fi/xml/Schedule/?area=1017&dt=".concat(parsePaivamaara);
                StartAsyncFetchMovies();
                counter = counter-1;
            }
            else if (counter == 5) {
                urlMovies = "http://www.finnkino.fi/xml/Schedule/?area=1041&dt=".concat(parsePaivamaara);
                StartAsyncFetchMovies();
                counter = counter-1;
            }
            else if (counter == 4) {
                urlMovies = "http://www.finnkino.fi/xml/Schedule/?area=1018&dt=".concat(parsePaivamaara);
                StartAsyncFetchMovies();
                counter = counter-1;
            }
            else if (counter == 3) {
                urlMovies = "http://www.finnkino.fi/xml/Schedule/?area=1019&dt=".concat(parsePaivamaara);
                StartAsyncFetchMovies();
                counter = counter-1;
            }
            else if (counter == 2) {
                urlMovies = "http://www.finnkino.fi/xml/Schedule/?area=1021&dt=".concat(parsePaivamaara);
                StartAsyncFetchMovies();
                counter = counter-1;
            }
            else if (counter == 1) {
                urlMovies = "http://www.finnkino.fi/xml/Schedule/?area=1022&dt=".concat(parsePaivamaara);
                StartAsyncFetchMovies();
                counter = counter-1;
            }
        }
        else if (counter != 0){
            counter = 0;
            StartAsyncFetchMovies();
        }
    }


    /* *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** */
    /* New AsyncTask with Fetch */
    /* *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** */
    public void StartAsyncFetch() {
        AsyncDownload task = new AsyncDownload(this);
        task.execute(urlAreas);
    }

    public void StartAsyncFetchMovies() {
        AsyncDownload task = new AsyncDownload(this);
        task.execute(urlMovies);
    }

    private static class AsyncDownload extends AsyncTask<String, Integer, Integer> {
        private WeakReference<MainActivity> activityWeakReference;

        AsyncDownload(MainActivity activity) {
            activityWeakReference = new WeakReference<MainActivity>(activity);

        }
        ArrayList<Integer> idt = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>(), starts = new ArrayList<>(), theatres = new ArrayList<>(), theatreids = new ArrayList<>();

        String rootElement;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            activity.progress.setVisibility(View.VISIBLE);
            activity.progress.setProgress(0);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            MainActivity activity = activityWeakReference.get();

            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.progress.setProgress(values[0],true);
        }


        @Override
        protected Integer doInBackground(String... strings) {
            String osoite = strings[0];
            System.out.println(osoite);
            rootElement = "";
            String uusiTeatteriNimi = "", temp;
            int uusiTeatteriId;
            int progressUpdate = 0;

            try {

                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document dokkari = builder.parse(osoite);
                dokkari.getDocumentElement().normalize();

                rootElement = dokkari.getDocumentElement().getNodeName();
                System.out.println(rootElement);
                NodeList nodeL;


                if (rootElement.equals("TheatreAreas")) {
                    System.out.println("TheatreAreas");
                    nodeL = dokkari.getDocumentElement().getElementsByTagName("TheatreArea");
                    progressUpdate = nodeL.getLength();
                    for (int i = 0; i < nodeL.getLength(); i++) {
                        publishProgress((i * 100) / progressUpdate);
                        Node yksikko = nodeL.item(i);

                        if (yksikko.getNodeType() == Node.ELEMENT_NODE) {
                            /* Elementti tietotyyppi valitaan if lausekkeessa.*/
                            Element element = (Element) yksikko;

                            /* Tilapäismuuttujaan asetetaan uuden teatterin, siis solmun kohdalla olevan solmun tagi "Name" */
                            uusiTeatteriNimi = element.getElementsByTagName("Name").item(0).getTextContent();
                            names.add(uusiTeatteriNimi);
                            System.out.println(uusiTeatteriNimi);

                            /* Id on tietotyyppiä String, muutetaan se temp -muuttujan kautta integeriksi */
                            temp = element.getElementsByTagName("ID").item(0).getTextContent();
                            uusiTeatteriId = Integer.parseInt(temp);
                            System.out.println("Id on " + uusiTeatteriId);
                            idt.add(uusiTeatteriId);
                        }
                    }
                }
                else {
                    nodeL = dokkari.getDocumentElement().getElementsByTagName("Show");
                    progressUpdate = nodeL.getLength();
                    String uusiElokuvaTitle, uusiTheatreAuditorium, uusiTheatreId;
                    for (int i = 0; i < nodeL.getLength(); i++) {
                        Node yksikko = nodeL.item(i);
                        publishProgress((i * 100) / progressUpdate);
                        if (yksikko.getNodeType() == Node.ELEMENT_NODE) {
                            /* Elementti tietotyyppi valitaan if lausekkeessa.*/
                            Element element = (Element) yksikko;

                            /* Tilapäismuuttujaan asetetaan uuden teatterin, siis solmun kohdalla olevan solmun tagi "Name" */
                            uusiElokuvaTitle = element.getElementsByTagName("Title").item(0).getTextContent();
                            titles.add(uusiElokuvaTitle);

                            /* Time and other elements split and parsed to usable format */
                            temp = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                            starts.add(temp);

                            uusiTheatreAuditorium = element.getElementsByTagName("TheatreAndAuditorium").item(0).getTextContent();
                            theatres.add(uusiTheatreAuditorium);
                            uusiTheatreId = element.getElementsByTagName("TheatreID").item(0).getTextContent();
                            theatreids.add(uusiTheatreId);
                        }
                    }
                }

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 2;
        }

        @Override
        protected void onPostExecute(Integer s) {

//            System.out.println("lol");
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }

            if (rootElement.equals("TheatreAreas")) {
                activity.setArrayListaan(idt, names);
            }
            else if (rootElement.equals("Schedule")){
                activity.parser.ParseData(titles,starts,theatres,theatreids);
                activity.setElokuvatListaan();
            }
            activity.progress.setVisibility(View.INVISIBLE);
            activity.fetchAsync();

        }
    }
    /* *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** */


    public void setArrayListaan(ArrayList<Integer> idt, ArrayList<String> names) {

        for (int x = 0; x < idt.size(); x++) {
            System.out.println(x);
            listaTeatterit.newTeatteri(names.get(x),idt.get(x));
        }
        doSpinnerTeatteri();
    }
    public void setElokuvatListaan() {

        listaElokuvat = elokuvatList.getInstance();
        listaElokuvat.applyFilters();
        int size = listaElokuvat.getSizeFromelokuvat();

        recyclerArray = new ArrayList<>();

        for (int x = 0; x < size; x++) {
            /* Recycler view alustetaan... */
            recyclerArray.add(new recyclerItem(listaElokuvat.getLine1fromIndex(x), listaElokuvat.getLine2fromIndex(x)));
        }
        rAdapter = new recyclerAdapter(recyclerArray);
        recyclerLayout = new LinearLayoutManager(this);
        recyclerElokuva.setLayoutManager(recyclerLayout);
        recyclerElokuva.setAdapter(rAdapter);
    }

    public void setTime() {
        if (buttonclicked == 1) {
            TextView textToClock = (TextView) findViewById(R.id.time1textView);
            time1 = Integer.toString(hour).concat(":").concat(Integer.toString(minute));
            int temporary = (hour * 60) + minute;
            textToClock.setText(time1);
            listaElokuvat.setFilterStart(temporary);
            setElokuvatListaan();
        }
        else if (buttonclicked == 2) {
            TextView textToClock = (TextView) findViewById(R.id.time2textView);
            time2 = Integer.toString(hour).concat(":").concat(Integer.toString(minute));
            textToClock.setText(time2);
            int temporary = (hour * 60) + minute;
            listaElokuvat.setFilterEnd(temporary);
            setElokuvatListaan();
        }
    }
    public void uusiHaku() {
        counter = 9;
        listaElokuvat.newArrayList();
        urlMovies = urlFetch();

        if (!urlMovies.equals("")) {
            fetchAsync();
        }
    }
    public void thisDayIs() {
        Calendar kalenteri = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        currentDateString = sdf.format(kalenteri.getTime()).toString();
    }

}
