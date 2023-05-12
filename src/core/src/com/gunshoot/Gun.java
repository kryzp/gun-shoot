package com.gunshoot;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Gun {

	public Gun() {
	}

	public abstract void update(float dt);
	public abstract void render(SpriteBatch b);
	public abstract void shoot();
	public abstract void dispose();
}
