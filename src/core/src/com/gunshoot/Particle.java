package com.gunshoot;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public abstract class Particle {

	public int generation = 0;

	public Sprite sprite;
	public float spriteRotation;
	
	public Vector2 position;
	public Vector2 direction;

	public float speed;
	public float angularSpeed;

	public float speedDecay;
	public float angularSpeedDecay;

	public float size;
	public float sizeDecay;
	
	public Particle() {
		this.spriteRotation = 0f;
		
		this.position = Vector2.Zero.cpy();

		this.speed = 0f;
		this.angularSpeed = 0f;

		this.speedDecay = 0f;
		this.angularSpeedDecay = 0f;

		this.size = 1f;
		this.sizeDecay = 0f;
	}

	public void update(float dt) {
		speed = MathUtils.lerp(speed, 0f, speedDecay);
		angularSpeed = MathUtils.lerp(angularSpeed, 0f, angularSpeedDecay);
		size = MathUtils.lerp(size, 0f, sizeDecay);
		position.add(direction.cpy().nor().scl(speed * dt));
		direction.rotateDeg(angularSpeed * dt);
	}

	public void render(SpriteBatch b) {
		sprite.setOriginBasedPosition(position.x, position.y);
		sprite.setScale(size);
		sprite.setRotation(direction.angleDeg() + spriteRotation);
		sprite.draw(b);
	}
}
