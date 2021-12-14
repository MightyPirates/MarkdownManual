# MarkdownManual

MarkdownManual is a library mod for Minecraft that allows painless definition of in-game manuals backed by Markdown
documents.

## License / Use in Modpacks

This mod is [licensed under the **MIT license**](LICENSE). All **assets are public domain**, unless otherwise stated;
all are free to be distributed as long as the license / source credits are kept. This means you can use this mod in any
mod pack **as you please**. I'd be happy to hear about you using it, though, just out of curiosity.

## Usage

In general, please refer to [the API](src/main/java/li/cil/manual/api), everything you need to know should be explained
in the Javadoc of the API classes and interfaces.

### Defining a new manual

To define a new manual, create an instance of [`Manual`](src/main/java/li/cil/manual/api/prefab/Manual.java) and
register it with the `ManualModel` registry. For convenience, the location of this registry is available in
the [`Constants`](src/main/java/li/cil/manual/api/util/Constants.java) class.

To get content into the newly created manual, you'll want to add
a [`ContentProvider`](src/main/java/li/cil/manual/api/provider/ContentProvider.java). A simple setup just needs to add
single [`NamespaceContentProvider`](src/main/java/li/cil/manual/api/prefab/provider/NamespaceContentProvider.java) for
the current mod, e.g.:

```java
final DeferredRegister<ContentProvider> contentProviders =
    DeferredRegister.create(ContentProvider.class, "your_mod_id");
    contentProviders.register("name_of_your_content_provider",
    () -> new NamespaceContentProvider("your_mod_id", "doc"))
```

Where `doc` is a path in your mod's assets, in this case your directory structure would be like this:

```text
your_mod_dev_dir
|-+ src
  |-+ main
    | ...
  |-+ resources
    |-+ assets
      |-+ your_mod_id
        |-+ doc
          |-+ en_us
            |-  index.md
            | ...
```

Usually you'll then also want to add an item representing this manual in the game. For a simple setup, subclass
the [`AbstractManualItem`](src/main/java/li/cil/manual/api/prefab/item/AbstractManualItem.java). This will provide you
with a default UI, as well. To customize the UI, see below.

### Defining content
The contents of manuals are regular Markdown pages. Manual rendering only supports a basic subset of markdown, including header scaling, italic, bold, monospace and lists.

It also supports links to other manual pages and has basic support for image rendering. They must explicitly use the `texture` protocol. Rendering blocks and items is also possible using the `block` and `item` protocols.

Examples:
```markdown
![Image tooltip](texture:example.png)
![Tooltip for furnace block](block:minecraft:furnace)
![Tooltip for TIS-3D casing item](item:tis3d:casing)
```

Mods may also add additional custom content rendering. For example, you could add a content renderer that renders recipes, if you were so inclined.

### Extending an existing manual

To extend an existing manual, register providers that match the manual to extend. For example, implement
a [`ContentProvider`](src/main/java/li/cil/manual/api/provider/ContentProvider.java) and override
the `matches(ManualModel)` method to return true for the manual to add content for. By default, providers will only
match manuals defined in the same namespace as themselves, i.e., defined by the same mod, as this is usually the desired
behavior.

### Adding tabs

To add additional entry points next to the manual (tabs), follow the same basic procedure as when extending a manual
with any other provider. Simply register a new [`Tab`](src/main/java/li/cil/manual/api/Tab.java) implementation with the
tab registry.

### Changing the manual UI

When using the `AbstractManualItem` or using the `ShowManualScreenEvent` the built-in manual screen implementation is
used. This implementation can be styled using implementations of
the [`ManualStyle`](src/main/java/li/cil/manual/api/ManualStyle.java)
and [`ManualScreenStyle`](src/main/java/li/cil/manual/api/ManualScreenStyle.java) interfaces. These allow changing
textures used in the screen (background, tab, scroll bar) as well as changing content rendering parameters such as
fonts, text color and so on.

To use a completely customized manual screen, you'll want to override `AbstractManualItem.showManualScreen()` or use a
custom item. In this case you'll need to query content from the `ManualModel` and use the [`Document`](src/main/java/li/cil/manual/client/document/Document.java)
class directly. See the implementation of the default `ManualScreen` for details on its usage.

### Gradle

To add a dependency to this mod for use in your mod, add the following to your `build.gradle`:

```groovy
repositories {
    maven {
        url 'https://cursemaven.com'
        content { includeGroup "curse.maven" }
    }
}
dependencies {
    implementation fg.deobf("curse.maven:markdownmanual-502485:3565800")
}
```
