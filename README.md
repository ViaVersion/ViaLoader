# ViaLoader
Easy to use ViaVersion, (and optional ViaBackwards, ViaRewind, ViaLegacy, ViaAprilFools and ViaBedrock) platform implementation.

ViaLoader is not usable by itself as a standalone software, as it is an implementation of a ViaVersion platform.

### Projects implementing ViaLoader
- [ViaProxy](https://github.com/ViaVersion/ViaProxy): Standalone proxy which allows players to join EVERY Minecraft server version (Classic, Alpha, Beta, Release, Bedrock).
- [ViaForge](https://github.com/ViaVersion/ViaForge): Client-side ViaVersion implementation for MinecraftForge and NeoForge.
- [ViaFabricPlus](https://github.com/ViaVersion/ViaFabricPlus): Fabric mod to connect to EVERY Minecraft server version (Release, Beta, Alpha, Classic, Snapshots, Bedrock) with QoL fixes to the gameplay.

## Releases
### Gradle/Maven
To use ViaLoader with Gradle/Maven you can use the ViaVersion maven server:
```groovy
repositories {
    maven { url "https://repo.viaversion.com" }
}

dependencies {
    implementation("com.viaversion:vialoader:x.x.x") // Get latest version from releases
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
        <groupId>com.viaversion</groupId>
        <artifactId>vialoader</artifactId>
        <version>x.x.x</version> <!-- Get latest version from releases -->
    </dependency>
</dependencies>
```

### Jar File
If you just want the latest jar file you can download it from [GitHub Actions](https://github.com/RaphiMC/ViaLoader/actions/workflows/build.yml) or the [ViaVersion Jenkins](https://ci.viaversion.com/view/All/job/ViaLoader/).

## Usage
To use ViaLoader in your project you need to decide what components of Via* you want to use.
ViaLoader is split into 6 different components:
- ViaVersion (Is the base component of ViaLoader [required])
- ViaBackwards (Allows older clients to join to newer server versions [needs ViaVersion])
- ViaRewind (Allows 1.8.x and 1.7.x clients to join to 1.9+ servers [needs ViaBackwards])
- ViaLegacy (Allows clients to join to <= 1.7.10 servers [needs ViaVersion])
- ViaAprilFools (Allows clients to join to some notable Minecraft snapshots [needs ViaBackwards])
- ViaBedrock (Allows clients to join to Bedrock edition servers [needs latest ViaVersion and ViaLoader snapshot versions])

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
implementation "com.viaversion:viaversion-common:5.3.2"
implementation "com.viaversion:viabackwards-common:5.3.2"
implementation "com.viaversion:viarewind-common:4.0.7"
implementation "net.raphimc:ViaLegacy:3.0.9"
implementation "com.viaversion:viaaprilfools-common:4.0.1"
implementation "net.raphimc:ViaBedrock:0.0.16-SNAPSHOT"
```

## Implementation
To implement ViaLoader into your project you need to initialize the Via* platforms first.  
ViaLoader provides a wrapper class with default values for that. To use a default value you can just pass ``null`` to that argument.
If you want to change the default value you should create your own class which extends the base class and overrides the methods you want to change.

The only default value you have to change is the ``VLLoader`` argument. The loader is used to register all the providers for the Via* platforms. 
Providers are used to provide information about the platform to ViaVersion when it's (almost) impossible to get them ourselves. 

To override the default you first create a new class which extends ``VLLoader`` and overrides the ``load`` method.  
Within the ``load`` method you have to register a ``VersionProvider`` implementation which will be used to determine the target server version of a given connection.
Here is an example implementation:
```java
public class CustomVLLoaderImpl extends VLLoader {

    @Override
    public void load() {
        super.load();

        Via.getManager().getProviders().use(VersionProvider.class, new BaseVersionProvider() {
            @Override
            public ProtocolVersion getClosestServerProtocol(UserConnection connection) {
                // Change the logic here to select the target server version
                return ProtocolVersion.v1_8;
            }
        });
    }
}
```
Then you have to create a new instance of your loader class and pass it to the ``ViaLoader.init`` call.

To do this you can call the ``ViaLoader.init()`` method somewhere suitable in your project (You can do that async) with your desired argument values:
```java
ViaLoader.init(null/*ViaPlatform*/, new CustomVLLoaderImpl(), null/*ViaInjector*/, null/*ViaCommandHandler*/, ViaBackwardsPlatformImpl::new, ViaRewindPlatforImpl::new, ViaLegacyPlatformImpl::new, ViaAprilFoolsPlatformImpl::new, ViaBedrockPlatformImpl::new);
```
Make sure to have all platform impls you need added to the init call. Every platform added there needs to be in your dependencies.

After you have initialized the Via* platforms you can start implementing ViaLoader into your project.

### Netty modifications

The most important part is the modification of your netty pipeline. This is needed for ViaVersion to translate the packets in both ways.

Our recommended way is to use the `VLPipeline` (for codec based pipelines) or the `VLLegacyPipeline` (for decoder/encoder pipelines) as it 
automatically handles the modifications required for all Via* platforms.

Here is an example implementation:
```java
public class CustomVLPipeline extends VLPipeline {

    public CustomVLPipeline(UserConnection connection) {
        super(connection);
    }

    // Replace these with the names of your pipeline components
    @Override
    protected String compressionCodecName() {
        return "compression_codec";
    }

    @Override
    protected String packetCodecName() {
        return "packet_codec";
    }

    @Override
    protected String lengthCodecName() {
        return "length_codec";
    }

}
```
The same can be done for the `VLLegacyPipeline` with similar functions for the decoder/encoder pairs.

Then you can add the Via* pipeline to your netty pipeline:
```java
final UserConnection connection = new UserConnectionImpl(channel, true/*clientside or serverside*/);
new ProtocolPipelineImpl(connection);

channel.pipeline().addLast(new CustomVLPipeline(connection));
```

Both `VLPipeline` and `VLLegacyPipeline` contain various functions allowing you to modify/wrap the existing pipeline elements,
if you need a more complex/dynamic pipeline setup you can also manually add the Via* handlers to the pipeline.
Here is an example implementation:
```java
final UserConnection connection = new UserConnectionImpl(channel, true/*clientside or serverside*/);
new ProtocolPipelineImpl(connection);

//channel.pipeline().addBefore("packet_decoder", VLLegacyPipeline.VIA_DECODER_NAME, new ViaDecoder(connection));
//channel.pipeline().addBefore("packet_encoder", VLLegacyPipeline.VIA_ENCODER_NAME, new ViaEncoder(connection));
channel.pipeline().addBefore("packet_codec", VLPipeline.VIA_CODEC_NAME, new ViaCodec(connection));
```
If you are using ViaLegacy, you should read its [README](https://github.com/ViaVersion/ViaLegacy?tab=readme-ov-file#vialegacy) to see what changes you need to make to the netty pipeline for it to work.

Depending on where you are implementing ViaLoader you might need to ensure that the pipeline is held in the correct order.
Minecraft clients modify the pipeline order when adding the compression handlers. You have to ensure that the Via* handlers are always on their correct location.

If you are using the `VLPipeline` or `VLLegacyPipeline` you can fire the `CompressionReorderEvent` once the compression handler is added to the pipeline.

Now you should have a working protocol translator implementation in your project.

## Configuring the protocol translation
To change the protocol translation settings/features you can look into the ViaLoader folder.
You can find 5 config files there depending on the platforms loaded:
- viaversion.yml (ViaVersion)
- viabackwards.yml (ViaBackwards)
- viarewind.yml (ViaRewind)
- vialegacy.yml (ViaLegacy)
- viabedrock.yml (ViaBedrock)

## Contact
If you encounter any issues, please report them on the
[issue tracker](https://github.com/ViaVersion/ViaLoader/issues).  
If you just want to talk or need help implementing ViaLoader feel free to join the ViaVersion
[Discord](https://discord.gg/viaversion).
