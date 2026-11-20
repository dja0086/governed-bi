# Cube project

Cube Cloud deployment 1 (`domcowboy.cubecloud.dev`) builds from this folder: repository `dja0086/governed-bi`, branch `main`, project directory `cube`.

- `cube.py`: configuration. No secrets; connection settings are Cube Cloud environment variables.
- `model/`: the data model Cube serves. Under D-003 this is generated output: the compiler will write it from `manifests/`. Until the compiler exists, the only file is `model/cubes/connection_check.yml`, a hand-written plumbing check that is not a dataset and carries no certified metric.

Do not hand-edit generated model files once the compiler writes them. Change the manifest instead.
