# Governance

One person owns this project. Governance exists to protect the numbers and the accounts, not to pace the work. Nothing here adds a wait that evidence does not require.

## Roles

| Role | Who | Does |
|---|---|---|
| Owner | Dominick Amirr | Decides scope, spending and certification. Holds every human role: finance owner, controller, NetSuite administrator |
| Builder | Claude (AI agent) | Drafts, tests, merges uncertified work, records everything |

## What the builder does without asking

- Opens a pull request for every change and merges it once checks pass.
- Drafts and edits manifests in `draft`, `mapped` or `reconciled` state.
- Writes code, tests, spike results and documents.
- Reads from NetSuite: reports, saved searches, metadata.

## What needs the owner

Five things only. Each is one decision, not a process.

| Needs the owner | Why |
|---|---|
| Certifying a metric, or changing a certified number | The owner vouches for the figure; the builder cannot vouch for its own work |
| Any write to NetSuite | It is the book of record. Custom read-only queries have standing approval within the limits in D-018 |
| Creating accounts, credentials or roles | They belong to the owner |
| Spending money | It is the owner's money |
| Changing this file | The builder does not set its own authority |

## Pace

- No calendar schedule. Work runs in dependency order as fast as evidence arrives.
- A gate is passed when its evidence exists, on whatever day that is.
- The only waits are real ones: a workspace not yet opened, a permission not yet granted, a test still running, an API limit.

## Records

| Record | File |
|---|---|
| Decisions and their evidence | `DECISIONS.md` |
| Current state and next step | `STATUS.md` |
| How to pick this work up | `HANDOFF.md` |
| Spike results | `phase0/results/` |

## Certification

A metric is certified only when its manifest is merged, its tie-out passes on a common snapshot, and the owner approves. A certified answer is one rendered by a server-delivered component that shows scope, metric version, as-of time and status. Anything an AI assistant writes or computes itself is uncertified.

## If more people join

Add them here with what they decide. Until then, no rule in this file assumes a second person.
