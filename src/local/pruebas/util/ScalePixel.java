package local.pruebas.util;

import android.content.Context;

public class ScalePixel {
	
	private Context mContexto;
	
	public ScalePixel() {
		
	}
	
	public ScalePixel(Context context) {
		mContexto = context;
	}
	
	public int getPixels(float dip) {
		if(mContexto == null) {
			return 0;
		} else {
			int pixels;
			final float SCALE = mContexto.getResources().getDisplayMetrics().scaledDensity;
			pixels = (int)(dip * SCALE + 0.5f);
			return pixels;
		}
	}
}
