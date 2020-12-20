/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.ui.map;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.embed.swing.SwingNode;
import javafx.scene.layout.StackPane;
import main.domain.Logic;
import main.ui.map.painters.RoutePainter;
import main.ui.map.painters.SelectionAdapter;
import main.ui.map.painters.SelectionPainter;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

/**
 *
 * @author J
 */
public class RunMap {
    
    public StackPane getMap(Logic logic, List<String> gpxFilePaths) {        
        final SwingNode swingNode = new SwingNode();        
        JXMapViewer mapViewer = new JXMapViewer();
        
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        setupLocalFileCache(tileFactory, mapViewer);
        
        List<List<GeoPosition>> positionLists = new ArrayList();
        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();        
        populateMapObjects(gpxFilePaths, logic, positionLists, painters);        
        prepareJXMapViewer(logic, positionLists, mapViewer,  painters);
        
        SwingUtilities.invokeLater(() -> {
            swingNode.setContent(mapViewer);
        });
        StackPane pane = prepareStackPane(swingNode);
        
        return pane;        
    }
    
    private void populateMapObjects(List<String> gpxFilePaths, Logic logic, List<List<GeoPosition>> positionLists, List<Painter<JXMapViewer>> painters) {
        for (String path: gpxFilePaths) {
            List<String> lines = logic.readGpxFile(path);
            List<GeoPosition> coordinates = logic.readCoordinates(lines);            
            positionLists.add(coordinates);
            RoutePainter routePainter = new RoutePainter(coordinates);
            painters.add(routePainter);
        }
    }
    
    private void setupLocalFileCache(DefaultTileFactory tileFactory, JXMapViewer mapViewer) {        
        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));
        mapViewer.setTileFactory(tileFactory);
        tileFactory.setThreadPoolSize(8);
    }
    
    private void prepareJXMapViewer(Logic logic, List<List<GeoPosition>> positionLists, JXMapViewer mapViewer,  List<Painter<JXMapViewer>> painters) {
        GeoPosition mapCenter = logic.getMapCenter(positionLists);
        mapViewer.setZoom(4);
        mapViewer.setAddressLocation(mapCenter);
        
        setMouseSupport(mapViewer);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);
    }
    
    private void setMouseSupport(JXMapViewer mapViewer) {
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);

        mapViewer.addMouseListener(new CenterMapListener(mapViewer));

        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        // Add a selection painter
        SelectionAdapter sa = new SelectionAdapter(mapViewer);
        SelectionPainter sp = new SelectionPainter(sa);
        mapViewer.addMouseListener(sa);
        mapViewer.addMouseMotionListener(sa);
        mapViewer.setOverlayPainter(sp);
    }
    
    private StackPane prepareStackPane(SwingNode swingNode) {
        StackPane pane = new StackPane();
        pane.getChildren().add(swingNode);
        pane.setMinSize(800, 600);
        
        return pane;
    }
}
