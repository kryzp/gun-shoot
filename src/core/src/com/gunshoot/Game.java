package com.gunshoot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {

	public static final float SCALE = 8f;

	private SpriteBatch batch;
	private Gun currentGun;
	private FrameBuffer renderTarget;
	//private ShaderProgram blurShader;
	private ShaderProgram postProcessShader;

	public static OrthographicCamera camera = null;

	@Override
	public void create() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(true, 1280f / SCALE, 720f / SCALE);
		currentGun = new Shotgun();
//		blurShader = new ShaderProgram(Gdx.files.internal("blur_vert.glsl").readString(), Gdx.files.internal("blur_frag.glsl").readString());
		postProcessShader = new ShaderProgram(Gdx.files.internal("post_proc_vert.glsl").readString(), Gdx.files.internal("post_proc_frag.glsl").readString());
		renderTarget = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
	}

	@Override
	public void render() {

		if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
			currentGun.shoot();
		}

		camera.position.x = MathUtils.lerp(camera.position.x, camera.viewportWidth / 2f, 0.2f);
		camera.position.y = MathUtils.lerp(camera.position.y, camera.viewportHeight / 2f, 0.2f);

		camera.update();
		currentGun.update(Gdx.graphics.getDeltaTime());

		renderTarget.begin();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		ScreenUtils.clear(0.11f, 0.11f, 0.14f, 1.0f);

		currentGun.render(batch);

		batch.end();
		renderTarget.end();

		batch.getProjectionMatrix().setToOrtho2D(0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		batch.begin();
		batch.setShader(postProcessShader);
		TextureRegion rr = new TextureRegion(renderTarget.getColorBufferTexture());
		rr.flip(false, true);
		batch.draw(rr, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.setShader(null);
		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		currentGun.dispose();
	}

	public static void shakeCamera() {
		camera.translate(((float)Math.random() - 0.5f) * 10f, ((float)Math.random() - 0.5f) * 10f);
	}
}
