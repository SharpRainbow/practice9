package ru.mirea.panin.mireaproject.ui.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.panin.mireaproject.R;
import ru.mirea.panin.mireaproject.databinding.FragmentMapsBinding;

public class MapsFragment extends Fragment {

    private FragmentMapsBinding binding;
    private GoogleMap map;
    private Button layers;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoContents(@NonNull Marker marker) {
                    LinearLayout info = new LinearLayout(requireContext());
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(requireContext());
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(requireContext());
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }

                @Nullable
                @Override
                public View getInfoWindow(@NonNull Marker marker) {
                    return null;
                }
            });
            setUpMap();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        layers = binding.changeLayerBtn;
        layers.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Change layer")
                    .setItems(new CharSequence[]{"Normal", "Hybrid", "Satellite", "Terrain"}, (dialogInterface, i) -> {
                        switch (i) {
                            case 0:
                                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                break;
                            case 1:
                                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                break;
                            case 2:
                                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                break;
                            case 3:
                                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                break;
                        }
                    });
            builder.show();
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void setUpMap() {
        List<LatLng> mirea = new ArrayList<>();
        mirea.add(new LatLng(55.6702, 37.4803));
        mirea.add(new LatLng(55.6617, 37.4777));
        mirea.add(new LatLng(55.7317, 37.5747));
        mirea.add(new LatLng(55.7957, 37.7010));
        mirea.add(new LatLng(55.7650, 37.7420));
        map.addMarker(new MarkerOptions().title("РТУ МИРЭА")
                .snippet("Дата основания: 1947 г.\n" +
                        "Адрес: просп. Вернадского, 78, стр. 4\n" +
                        "Координаты: 55.6702, 37.4803").position(mirea.get(0)));

        map.addMarker(new MarkerOptions().title("РТУ МИРЭА")
                .snippet("Дата основания: 1900 г.\n" +
                        "Адрес: просп. Вернадского, 86\n" +
                        "Координаты: 55.6617, 37.4777").position(mirea.get(1)));

        map.addMarker(new MarkerOptions().title("РТУ МИРЭА")
                .snippet("Дата основания: 1900 г.\n" +
                        "Адрес: Малая Пироговская ул., 1, стр. 5\n" +
                        "Координаты: 55.7317, 37.5747").position(mirea.get(2)));

        map.addMarker(new MarkerOptions().title("РТУ МИРЭА")
                .snippet("Дата основания: 1936 г.\n" +
                        "Адрес: ул. Стромынка, 20\n" +
                        "Координаты: 55.7957, 37.7010").position(mirea.get(3)));
        map.addMarker(new MarkerOptions().title("РТУ МИРЭА")
                .snippet("Дата основания: 1970 г.\n" +
                        "Адрес: 5-я ул. Соколиной Горы, 22\n" +
                        "Координаты: 55.7650, 37.7420").position(mirea.get(4)));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                mirea.get(0)).zoom(15).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}