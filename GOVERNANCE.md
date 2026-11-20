# Governance

## Roles

| Role | Who | Decides | Does |
|---|---|---|---|
| Owner | Dominick Amirr | Priorities, budget, gate go/no-go, scope | Approves merges until a second reviewer exists |
| Builder | Claude (AI agent) | Nothing binding | Drafts all manifests, code, tests and documents as pull requests |
| Finance owner | To be named | Metric definitions, certification, tolerances | Reviews tie-outs; approves any change to a number |
| Client controller | To be named | Client mappings, sign-off at certification | Answers mapping questions; samples figures against NetSuite |
| NetSuite administrator | To be named | Roles and integration access in NetSuite | Creates the read-only role; deploys scripts when the seconds lane is built |

One person may hold more than one role during phase 0, except that nobody approves their own change.

## Approval rules

| Change | Example | Approver |
|---|---|---|
| Additive | New dimension, new metric | Owner |
| Corrective | Fix that changes a number | Finance owner |
| Breaking | Definition or grain change | Finance owner and client controller |
| Client mapping | Account or segment mapping | Client controller |
| Platform | Schema, compiler, adapter, CI | Owner |
| Access | Roles, credentials, isolation | Owner |

- The author never approves, whether a person or an AI agent.
- Every change is a pull request. `main` is protected: one approving review and passing checks.
- A change to a certified metric creates a new version. Closed periods are restated only under the restatement policy (to be written in phase 1).

## Gates

Work is funded one phase at a time. Each phase ends at a gate with a written gate pack: evidence, measured effort, forecast, and a go/no-go recommendation. The owner decides.

## Cadence

- Every working session: `STATUS.md` updated.
- Weekly: owner reviews `STATUS.md` and open decisions.
- At each gate: gate review.

## Records

| Record | File |
|---|---|
| Decisions and their evidence | `DECISIONS.md` |
| Current state and next step | `STATUS.md` |
| How to pick this work up | `HANDOFF.md` |
| Spike results | `phase0/` |

## Certification

A metric is certified only when its manifest is merged, its tie-out passes on a common snapshot, and the finance owner has approved. A certified answer is one rendered by a server-delivered component that shows scope, metric version, as-of time and status. Anything an AI assistant writes or computes itself is uncertified.
