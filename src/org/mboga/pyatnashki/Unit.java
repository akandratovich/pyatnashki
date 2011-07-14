package org.mboga.pyatnashki;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

public class Unit extends Sprite {
	private int num;
	private int value;
	
	public Unit(float x, float y, TextureRegion tr) {
		super(x*80, y*80, tr);
		num = (int) (x + 4*y);
		value = (int) (x + 4*y);
	}
	
	public int value() {
		return value;
	}
	
	public int position() {
		return getLX() + 4*getLY();
	}
	
	public int getLX() {
		return num % 4;
	}
	
	public int getLY() {
		return num / 4;
	}
	
	@Override
	public void setPosition(float _x, float _y) {
		num = (int) (_x + 4*_y);
		super.setPosition(_x*80, _y*80);
	}
	
	public void drag(float x, float y, Unit empty) {
		if (empty.getLX() == getLX()) {
			float upy = 0;
			float downy = 0;
			float cy = y;
			
			if (getLY() < empty.getLY()) {
				upy = getLY()*80 + 40;
				downy = empty.getLY()*80 + 40;
			} else {
				upy = empty.getLY()*80 + 40;
				downy = getLY()*80 + 40;
			}
			
			if (y < upy)
				cy = upy;
			if (y > downy)
				cy = downy;
			
			super.setPosition(getX(), cy - 40);
		} else if (empty.getLY() == getLY()) {
			float upy = 0;
			float downy = 0;
			float cy = x;
			
			if (getLX() < empty.getLX()) {
				upy = getLX()*80 + 40;
				downy = empty.getLX()*80 + 40;
			} else {
				upy = empty.getLX()*80 + 40;
				downy = getLX()*80 + 40;
			}
			
			if (x < upy)
				cy = upy;
			if (x > downy)
				cy = downy;
			
			super.setPosition(cy - 40, getY());
		}
	}
	
	public boolean revert() {
		if (Math.abs(getLX()*80 - getX()) + Math.abs(getLY()*80 - getY()) > 40)
			return false;
		return true;
	}
}
