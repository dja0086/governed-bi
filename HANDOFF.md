# Handoff

How anyone, person or AI agent, picks this project up cold.

## In ten minutes

1. `README.md`: what this is and the five rules.
2. `STATUS.md`: where it stands and the next step.
3. `DECISIONS.md`: what is settled and what is open.

## In an hour

4. `GOVERNANCE.md`: who decides what.
5. `docs/implementation-plan.md`: phases and gates to go-live.
6. `docs/council-review-followup.md`: the latest review and its rulings.
7. `phase0/SPIKES.md`: what must be proven before the build is funded.

## What you inherit

| Area | State |
|---|---|
| Requirements | Stated by the owner; see the implementation plan |
| Architecture | Chosen on documents, not tested. Phase 0 tests two stacks |
| Definitions | Two draft manifests (gross orders, P&L). Three more to hand-write |
| Code | A manifest validator and CI. No compiler yet |
| Access | NetSuite account TSTDRV1755417 through the owner's connector. No warehouse or Cube workspace yet |
| People | One owner who holds every human role, and one AI builder |

## What is not known

Everything in `phase0/SPIKES.md` is unproven. Do not build on any of it as if it were fact.

## Handing off again

Before you stop: update `STATUS.md`, record decisions in `DECISIONS.md`, push your branch, and open or update the pull request. If it is not in the repository, the next person does not have it.
