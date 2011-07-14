package org.mboga.pyatnashki;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.source.AssetTextureSource;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.view.Menu;
import android.view.MenuItem;

public class PyatnashkiActivity extends BaseGameActivity {
	private Camera mCamera;
	private float width = 320;
	private float height = 320;
	
	private Plot plot = null;
	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, width, height);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(width, height), mCamera));
	}

	@Override
	public void onLoadResources() {
		Texture texture = new Texture(512, 512, TextureOptions.DEFAULT);
		texture.addTextureSource(new AssetTextureSource(this, "plot.png"), 0, 0);
		
		this.plot = new Plot(texture, this);
		this.mEngine.getTextureManager().loadTexture(texture);
	}

	@Override
	public Scene onLoadScene() {
		Scene scene = new Scene();
		
		plot.attach(scene);
		scene.setTouchAreaBindingEnabled(true);
		
		return scene;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 1, Menu.NONE, "Shuffle");
		menu.add(Menu.NONE, 2, Menu.NONE, "Exit");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onMenuItemSelected(int id, MenuItem item) {
		switch(item.getItemId()) {
			case 1:
				plot.randomize();
				return true;
			case 2:
				finish();
				return true;
			default:
				return super.onMenuItemSelected(id, item);
		}
	}
	
	@Override
	public void onLoadComplete() {}

}
