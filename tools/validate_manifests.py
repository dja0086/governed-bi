"""Validate every manifest in manifests/ against spec/manifest.schema.json.

Usage: python tools/validate_manifests.py
Exits non-zero if any manifest is invalid.
"""
import json
import pathlib
import sys

import yaml
from jsonschema import Draft202012Validator

ROOT = pathlib.Path(__file__).resolve().parent.parent
SCHEMA = json.loads((ROOT / "spec" / "manifest.schema.json").read_text())


def main() -> int:
    validator = Draft202012Validator(SCHEMA)
    failures = 0
    files = sorted((ROOT / "manifests").glob("*.yaml"))
    if not files:
        print("no manifests found")
        return 1
    for path in files:
        doc = yaml.safe_load(path.read_text())
        errors = sorted(validator.iter_errors(doc), key=lambda e: list(e.path))
        if doc.get("dataset") != path.stem:
            errors.append(f"dataset name '{doc.get('dataset')}' does not match file name")
        ids = [m.get("id") for m in doc.get("metrics", [])]
        if len(ids) != len(set(ids)):
            errors.append("duplicate metric ids")
        if errors:
            failures += 1
            print(f"FAIL {path.name}")
            for err in errors:
                where = "/".join(str(p) for p in getattr(err, "path", [])) or "(root)"
                print(f"  {where}: {getattr(err, 'message', err)}")
        else:
            print(f"ok   {path.name}")
    return 1 if failures else 0


if __name__ == "__main__":
    sys.exit(main())
