# 마크 그림 도우미

ArtMap Workshop `1.9.20-alpha-items.1`의 분석 및 수정 작업 폴더입니다.
앞으로 관련 코드와 리소스 수정은 이 폴더 안에서 진행합니다.

| 경로 | 용도 |
|---|---|
| `original/` | 첨부된 원본 JAR 보관. 직접 수정하지 않음 |
| `src/main/java/com/easttg/artmapworkshop/` | 디컴파일한 Java 파일 32개. 코드 수정 위치 |
| `src/main/resources/` | 팔레트, 언어 JSON, 모드 정보 등 리소스 수정 위치 |
| `docs/분석.md` | 구조, 실행 흐름, 수정 시 확인할 사항 |
| `docs/original-MANIFEST.MF` | 원본 빌드 메타데이터 참고 자료 |

현재 코드는 원본 소스가 아닌 분석용 복원 결과입니다. Minecraft 1.21.4 / Fabric / Java 21 대상이며, 빌드 설정·매핑·의존성 복원과 실제 게임 검증이 필요합니다. 아직 빌드 가능한 프로젝트나 기능 수정 완료본은 아닙니다.

원본의 내장 라이브러리 6개는 `original/`의 JAR 안에 보존되어 있습니다.

원본 SHA-256: `202268b1ffacdd85c849171929fd1fc97eb82bba082e9040630e83097af8f4c5`

상세 내용은 [분석 문서](docs/분석.md)를 참조하세요.
