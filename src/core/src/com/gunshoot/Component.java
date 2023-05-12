package com.gunshoot;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Component {
	public Sprite sprite;
	public Vector2 position;
	public float rotation;

	public Component(TextureRegion region) {
		sprite = new Sprite(region);
		sprite.setScale(1f, -1f);
		rotation = 0f;
		position = new Vector2(Vector2.Zero);
	}

	public void render(SpriteBatch b) {
		sprite.setOriginBasedPosition(position.x, position.y);
		sprite.setRotation(rotation);
		sprite.draw(b);
	}
}
