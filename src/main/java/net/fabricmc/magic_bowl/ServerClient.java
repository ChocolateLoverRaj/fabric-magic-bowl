package net.fabricmc.magic_bowl;

public class ServerClient<T> {
  private T serverValue;
  private T clientValue;

  public ServerClient(T initialValue) {
    serverValue = clientValue = initialValue;
  }

  public T get(boolean isClient) {
    return isClient ? clientValue : serverValue;
  }

  public void set(boolean isClient, T value) {
    if (isClient)
      clientValue = value;
    else
      serverValue = value;
  }
}
