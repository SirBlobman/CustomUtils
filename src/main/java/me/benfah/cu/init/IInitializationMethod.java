package me.benfah.cu.init;

import me.benfah.cu.api.InitializationMethodProvider;
import me.benfah.cu.util.InitializationState;

public interface IInitializationMethod
{
	public void init(boolean forceUpload);

	public void initResourcePack(boolean forceUpload);

	public InitializationState getInitializationState();
}
