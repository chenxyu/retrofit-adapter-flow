`Retrofit` 返回 `Flow<out T>`

# 依赖版本
kotlinx-coroutines-android:1.3.5

retrofit:2.8.1

# Gradle 依赖

1.root build.gradle

```kotlin
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

2.app build.gradle

```kotlin
dependencies {
	implementation 'com.github.chenxyu:retrofit-adapter-flow:1.0.0'
}
```

# 使用方法

```kotlin
@POST(API.LOGIN)
fun login(@Body any: Any): Flow<User>

Retrofit.Builder()
    ...
    .addCallAdapterFactory(FlowCallAdapterFactory())
    .build()
```
