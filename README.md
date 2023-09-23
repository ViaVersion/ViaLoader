# ViaLoader
Easy to use ViaVersion, (and optional ViaBackwards, ViaRewind, ViaLegacy, ViaAprilFools and ViaBedrock) platform implementation.

ViaLoader is not usable by itself as a standalone software, as it is an implementation of a ViaVersion platform.

### Projects implementing ViaLoader
- [ViaProxy](https://github.com/ViaVersion/ViaProxy): Standalone proxy which allows players to join EVERY Minecraft server version (Classic, Alpha, Beta, Release, Bedrock).
- [ViaForge](https://github.com/ViaVersion/ViaForge): Client-side Implementation of ViaVersion, ViaBackwards and ViaRewind for Legacy Minecraft Forge
- [ViaFabricPlus](https://github.com/ViaVersion/ViaFabricPlus): Fabric mod to connect to EVERY Minecraft server version (Release, Beta, Alpha, Classic, Snapshots, Bedrock) with QoL fixes to the gameplay.

## Releases
### Gradle/Maven
To use ViaLoader with Gradle/Maven you can use the ViaVersion maven server:
```groovy
repositories {
    maven { url "https://repo.viaversion.com" }
}

dependencies {
    implementation("net.raphimc:ViaLoader:2.2.9") // Get latest version from releases
}
```

```xml
<repositories>
    <repository>
        <id>viaversion</id>
        <url>https://repo.viaversion.com</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>net.raphimc</groupId>
        <artifactId>ViaLoader</artifactId>
        <version>2.2.9</version> <!-- Get latest version from releases -->
    </dependency>
</dependencies>
```

### Jar File
If you just want the latest jar file you can download it from this [Jenkins](https://ci.viaversion.com/view/All/job/ViaLoader/).

## Usage
To use ViaLoader in your project you need to decide what components of Via* you want to use.
ViaLoader is split into 6 different components:
- ViaVersion (Is the base component of ViaLoader [required])
- ViaBackwards (Allows older clients to join to newer server versions [needs ViaVersion])
- ViaRewind (Allows 1.8.x and 1.7.x clients to join to 1.9+ servers [needs ViaBackwards])
- ViaLegacy (Allows clients to join to <= 1.7.10 servers [needs ViaVersion])
- ViaAprilFools (Allows clients to join to some notable Minecraft snapshots [needs ViaBackwards])
- ViaBedrock (Allows clients to join to Bedrock edition servers [needs ViaVersion])

In case you want to include ViaBedrock, you have to add the Lenni0451 maven repository to your build script:
```groovy
repositories {
    maven {
        name = "Lenni0451"
        url = "https://maven.lenni0451.net/everything"
    }
}
```
Here is an example dependency configuration for all components:
```groovy
implementation "com.viaversion:viaversion:4.8.0"
implementation("com.viaversion:viabackwards-common:4.8.0") {
    exclude group: "com.viaversion", module: "viaversion" // Exclude transitive dependency. Include manually for more control
    exclude group: "io.netty", module: "netty-all" // Don't include the outdated netty version
}
implementation "com.viaversion:viarewind-common:3.0.0"
implementation "net.raphimc:ViaLegacy:2.2.19"
implementation "net.raphimc:ViaAprilFools:2.0.9"
implementation "net.raphimc:ViaBedrock:0.0.3-SNAPSHOT"
```

## Implementation
To implement ViaLoader into your project you need to initialize the Via* platforms first.  
ViaLoader provides a wrapper class with default values for that. To use a default value you can just pass ``null`` to that argument.
If you want to change the default value you should create your own class which extends the base class and overrides the methods you want to change.

The only default value you have to change is the ``VLLoader`` argument. The loader is used to register all the providers for the Via* platforms.  
To override the default you first create a new class which extends ``VLLoader`` and overrides the ``load`` method (Make sure to call the super method).  
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
Then you have to create a new instance of your loader class and pass it to the ``ViaLoader.init`` call.

To do this you can call the ``ViaLoader.init()`` method somewhere suitable in your project (You can do that async) with your desired argument values:
```java
ViaLoader.init(null/*ViaPlatform*/, new CustomVLLoaderImpl(), null/*ViaInjector*/, null/*ViaCommandHandler*/, ViaBackwardsPlatformImpl::new, ViaLegacyPlatformImpl::new, ViaAprilFoolsPlatformImpl::new);
```

After you have initialized the Via* platforms you can start implementing ViaLoader into your project.

The most important part is the modification of your netty pipeline. This is needed for ViaVersion to translate the packets in both ways.
Here is an example implementation:
```java
final UserConnection user = new UserConnectionImpl(channel, true);
new ProtocolPipelineImpl(user);

channel.pipeline().addBefore("packet_codec", VLPipeline.VIA_CODEC_NAME, new ViaCodec(user));
```
If you are using ViaLegacy, you should read its README to see what changes you need to make to the netty pipeline for it to work.
Depending on where you are implementing ViaLoader you might need to ensure that the pipeline is held in the correct order.
Minecraft clients modify the pipeline order when adding the compression handlers. You have to ensure that the Via* handlers are always on their correct location.

Now you should have a working protocol translator implementation in your project.

## Configuring the protocol translation
To change the protocol translation settings/features you can look into the ViaLoader folder.
You can find 5 config files there depending on the platforms loaded:
- viaversion.yml (ViaVersion)
- config.yml (ViaBackwards)
- viarewind.yml (ViaRewind)
- vialegacy.yml (ViaLegacy)
- viabedrock.yml (ViaBedrock)

## Contact
If you encounter any issues, please report them on the
[issue tracker](https://github.com/ViaVersion/ViaLoader/issues).  
If you just want to talk or need help implementing ViaLoader feel free to join the ViaVersion
[Discord](https://discord.gg/viaversion).
