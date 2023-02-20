# ViaProtocolHack
Easy to use ViaVersion, (and optional ViaBackwards, ViaRewind and ViaLegacy) platform implementation.

ViaProtocolHack is not usable by itself as a standalone software, as it is an implementation of a ViaVersion platform.
ViaProtocolHack is intended to be implemented in a ViaVersion based protocol translator.

### Projects implementing ViaProtocolHack
- [ViaProxy](https://github.com/RaphiMC/ViaProxy): Standalone proxy which uses ViaVersion to translate between minecraft versions. Allows Minecraft 1.7+ clients to join to any version server.

## Releases
### Gradle/Maven
To use ViaProtocolHack with Gradle/Maven you can use this [Maven server](https://maven.lenni0451.net/#/releases/net/raphimc/ViaProtocolHack) or [Jitpack](https://jitpack.io/#RaphiMC/ViaProtocolHack).  
You can also find instructions how to implement it into your build script there.

### Jar File
If you just want the latest jar file you can download it from this [Jenkins](https://build.lenni0451.net/job/ViaProtocolHack/).

## Usage
To use ViaProtocolHack in your project you need to decide what components of Via* you want to use.
ViaProtocolHack is split into 4 different components:
- ViaVersion (Is the base component of ViaProtocolHack [required])
- ViaBackwards (Allows older clients to join to newer server versions [needs ViaVersion])
- ViaRewind (Allows 1.8.x and 1.7.x clients to join to 1.9+ servers [needs ViaBackwards])
- ViaLegacy (Allows clients to join to <= 1.7.10 servers [needs ViaBackwards])

To include ViaVersion/ViaBackwards/ViaRewind you have to add the ViaVersion maven repository to your build script:
```groovy
repositories {
    maven {
        name = "ViaVersion"
        url "https://repo.viaversion.com"
    }
}
```
To include ViaLegacy you can check out the [README](https://github.com/RaphiMC/ViaLegacy/blob/main/README.md#releases) of ViaLegacy.

Here is an example dependency configuration for all components:
```groovy
implementation "com.viaversion:viaversion:4.5.2-SNAPSHOT"
implementation("com.viaversion:viabackwards-common:4.5.2-SNAPSHOT") {
    exclude group: "com.viaversion", module: "viaversion" // Exclude transitive dependency. Include manually for more control
    exclude group: "io.netty", module: "netty-all" // Don't include the outdated netty version
}
implementation "com.viaversion:viarewind-core:2.0.3-SNAPSHOT"
implementation "net.raphimc:ViaLegacy:2.0.2"
```

## Implementation
To implement ViaProtocolHack into your project you need to initialize the Via* platforms first.  
ViaProtocolHack provides a wrapper class with default values for that. To use a default value you can just pass ``null`` to that argument.
If you want to change the default value you should create your own class which extends the base class and overrides the methods you want to change.

The only default value you have to change is the ``VPLoader`` argument. The loader is used to register all the providers for the Via* platforms.  
To override the default you first create a new class which extends ``VPLoader`` and overrides the ``load`` method (Make sure to call the super method).  
Within the ``load`` method you have to register a ``VersionProvider`` implementation which will be used to determine the target server version of a given connection.
Here is an example implementation:
```java
Via.getManager().getProviders().use(VersionProvider.class, new BaseVersionProvider() {
    @Override
    public int getClosestServerProtocol(UserConnection connection) {
            return ProtocolVersion.v1_8.getVersion(); // Change the logic here to select the target server version
    }
});
```
Then you have to create a new instance of your loader class and pass it to the ``ViaProtocolHack.init`` call.

To do this you can call the ``ViaProtocolHack.init()`` method somewhere suitable in your project (You can do that async) with your desired argument values:
```java
ViaProtocolHack.init(null/*ViaPlatform*/, new CustomVPLoaderImpl(), null/*ViaInjector*/, null/*ViaCommandHandler*/, ViaBackwardsPlatformImpl::new, ViaLegacyPlatformImpl::new);
```

After you have initialized the Via* platforms you can start implementing ViaProtocolHack into your project.

The most important part is the modification of your netty pipeline. This is needed for ViaVersion to translate the packets in both ways.
Here is an example implementation:
```java
final UserConnection user = new UserConnectionImpl(channel, true);
new ProtocolPipelineImpl(user);

channel.pipeline().addBefore("encoder", ViaPipeline.HANDLER_ENCODER_NAME, new ViaEncodeHandler(user));
channel.pipeline().addBefore("decoder", ViaPipeline.HANDLER_DECODER_NAME, new ViaDecodeHandler(user));
```
If you are using ViaLegacy, you should read its README to see what changes you need to make to the netty pipeline for it to work.
Depending on where you are implementing ViaProtocolHack you might need to ensure that the pipeline is held in the correct order.
Minecraft clients modify the pipeline order when adding the compression handlers. You have to ensure that the Via* handlers are always on their correct location.

Now you should have a working protocol translator implementation in your project.

## Contact
If you encounter any issues, please report them on the
[issue tracker](https://github.com/RaphiMC/ViaProtocolHack/issues).  
If you just want to talk or need help implementing ViaProtocolHack feel free to join my
[Discord](https://discord.gg/dCzT9XHEWu).
