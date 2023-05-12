package com.gunshoot;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class SparkParticle extends Particle {

	private Sprite sprite;

	public Vector2 position;
	public Vector2 direction;

	public float speed;
	public float angularSpeed;

	public float speedDecay;
	public float angularSpeedDecay;

	public float size;
	public float sizeDecay;

	public SparkParticle(TextureRegion region) {
		super();

		this.position = Vector2.Zero.cpy();

		this.speed = 0f;
		this.angularSpeed = 0f;

		this.speedDecay = 0f;
		this.angularSpeedDecay = 0f;

		this.size = 1f;
		this.sizeDecay = 0f;

		this.sprite = new Sprite(region);
		this.sprite.setScale(size);
	}

	@Override
	public void update(float dt) {
		speed = MathUtils.lerp(speed, 0f, speedDecay);
		angularSpeed = MathUtils.lerp(angularSpeed, 0f, angularSpeedDecay);
		size = MathUtils.lerp(size, 0f, sizeDecay);
		position.add(direction.cpy().nor().scl(speed * dt));
		direction.rotateDeg(angularSpeed * dt);
	}

	@Override
	public void render(SpriteBatch b) {
		sprite.setOriginBasedPosition(position.x, position.y);
		sprite.setScale(size);
		sprite.setRotation(direction.angleDeg() - 90f);
		sprite.draw(b);
	}
}