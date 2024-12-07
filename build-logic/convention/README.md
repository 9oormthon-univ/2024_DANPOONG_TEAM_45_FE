# 컨벤션 플러그인

`build-logic` 폴더는 프로젝트-specific 컨벤션 플러그인을 정의하며, 이는 공통 모듈 구성을 위한 단일 소스를 유지하는 데 사용됩니다.

이 접근 방식은 주로
[https://developer.squareup.com/blog/herding-elephants/](https://developer.squareup.com/blog/herding-elephants/)
와
[https://github.com/jjohannes/idiomatic-gradle](https://github.com/jjohannes/idiomatic-gradle)에서 영향을 받았습니다.

`build-logic`에서 컨벤션 플러그인을 설정함으로써 중복된 빌드 스크립트 설정이나 복잡한 `subproject` 구성을 피할 수 있으며, `buildSrc` 디렉토리의 단점도 방지할 수 있습니다.

`build-logic`는 루트의 [`settings.gradle.kts`](../settings.gradle.kts) 파일에 설정된 대로 포함된 빌드입니다.

`build-logic` 안에는 `convention` 모듈이 있으며, 이는 모든 일반 모듈이 자신을 구성하는 데 사용할 수 있는 플러그인을 정의합니다.

또한, `build-logic`에는 플러그인 간에 로직을 공유하는 데 사용되는 여러 개의 `Kotlin` 파일이 포함되어 있으며, 이는 Android 구성 요소(라이브러리와 애플리케이션)에 대한 공유 코드를 구성할 때 유용합니다.

이 플러그인들은 *덧셈적*이고 *구성 가능*하며, 가능한 한 하나의 책임만을 수행하도록 설계되었습니다. 모듈은 필요한 구성만 선택하여 사용할 수 있습니다. 만약 모듈에 대해 공통 코드가 없는 일회성 로직이 필요하다면, 이를 컨벤션 플러그인에 정의하기보다는 모듈의 `build.gradle` 파일에 직접 정의하는 것이 좋습니다.

현재 컨벤션 플러그인 목록:

- [`com.multi.module.android`](convention/src/main/kotlin/AndroidApplicationConventionPlugin.kt),
  [`com.multi.module.library`](convention/src/main/kotlin/AndroidLibraryConventionPlugin.kt)
