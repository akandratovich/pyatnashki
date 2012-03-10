package org.mboga.pyatnashki;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Plot {
  private Unit[][] palette;
  private Unit empty;
  private List<Unit> objs;
  
  private AlertDialog alert;
  private int count = 0;
  private boolean finish = false;
  
  public Plot(Texture texture, Activity activity) {
    palette = new Unit[4][4];
    objs = new ArrayList<Unit>();
    empty = null;
    
    alert = new AlertDialog.Builder(activity).setCancelable(false)
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
          }
        })
        .setNeutralButton("Shuffle", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            randomize();
            dialog.cancel();
          }
        }).create();
    
    init(texture);
    randomize();
  }
  
  private void init(Texture texture) {
    for (int i = 0; i < 4; i++)
      for (int j = 0; j < 4; j++) {
        TextureRegion tr = TextureRegionFactory.extractFromTexture(texture, i*80, j*80, 80, 80);
        objs.add(new Unit(i, j, tr) {
          @Override
          public boolean onAreaTouched(TouchEvent ev, float x, float y) {
            if (!canMove(this) || finish)
              return super.onAreaTouched(ev, x, y);
            
            if (ev.isActionUp()) {
              if (revert()) {
                setPosition(getLX(), getLY());
              } else {
                int ex = empty.getLX();
                int ey = empty.getLY();
                
                empty.setPosition(getLX(), getLY());
                setPosition(ex, ey);
                
                count++;
                
                check();
              }
              return true;
            }
            
            this.drag(ev.getX(), ev.getY(), empty);
            return true;
          }
        });
      }
    
    empty = objs.get(15);
  }
  
  private void check() {
    for (Unit u : objs)
      if (u.value() != u.position())
        return;
    
    finish = true;
    alert.setMessage("Congratulations!\nYour score is " + count + ".");
    alert.show();
  }
  
  private boolean canMove(Unit u) {
    
    if (u == empty)
      return false;
    
    int ex = empty.getLX();
    int ey = empty.getLY();
    
    if (u.getLX() == ex)
      if (Math.abs(u.getLY() - ey) < 2)
        return true;
    
    if (u.getLY() == ey)
      if (Math.abs(u.getLX() - ex) < 2)
        return true;
    
    return false;
  }
  
  public void randomize() {
    do {
      Collections.shuffle(objs);
      for (int i = 0; i < 4; i++)
        for (int j = 0; j < 4; j++) {
          Unit u = objs.get(i*4 + j);
          u.setPosition(i, j);
          palette[i][j] = u;
        }
    } while(!valid());
    
    finish = false;
    count = 0;
  }
  
  private boolean valid() {
    int sum = empty.getLY() + 1;
    for (int i = 0; i < 16; i++) {
      Unit u = palette[x(i)][y(i)];
      if (u == empty)
        continue;
      
      int k = 0;
      for (int j = i + 1; j < 16; j++) {
        Unit u2 = palette[x(j)][y(j)];
        if (u2 != empty && u2.value() < u.value())
          k++;
      }
      sum += k;
    }
    
    if (sum % 2 == 0)
      return true;
    
    return false;
  }
  
  private static int x(int n) {
    return n % 4;
  }
  
  private static int y(int n) {
    return n / 4;
  }
  
  public Unit getEmpty() {
    return empty;
  }
  
  public void attach(Scene s) {
    for (Unit u : objs) {
      s.attachChild(u);
      s.registerTouchArea(u);
    }
  }
}
