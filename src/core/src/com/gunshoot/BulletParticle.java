package com.gunshoot;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class BulletParticle extends Particle {

	private Sprite sprite;

	public BulletParticle(TextureRegion region) {
		super();

		this.sprite = new Sprite(region);
	}

	@Override
	public void update(float dt) {
	}

	@Override
	public void render(SpriteBatch b) {
	}
}
